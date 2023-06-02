package com.example.demo.service.csv;

import com.example.demo.domain.LogImport;
import com.example.demo.domain.enumeration.LogImportStatus;
import com.example.demo.repository.LogImportRepository;
import com.example.demo.service.app.LogImportService;
import com.google.common.io.ByteSource;
import io.github.avew.CsvewResultReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCsvRequestHandler {

    private final LogImportService logImportService;
    private final LogImportRepository repository;

    @Job(name = "Processing csv import %0", retries = 2)
    public void run(String id, JobContext jobContext) throws IOException {
        LogImport logImport = repository.findById(id).get();
        CsvUserReader reader = new CsvUserReader();
        InputStream inputStream = ByteSource.wrap(Base64.getDecoder().decode(logImport.getBase64())).openStream();
        if (logImport.getCounter() == 0) {
            CsvewResultReader<CsvUserValueDTO> read = reader.process(0, inputStream);
            int total = read.getCount();
            if (!read.isError()) {
                logImportService.updateTotalById(id, total);
                processing(id, jobContext, read, 0, total);
            }
        } else {
            CsvewResultReader<CsvUserValueDTO> read = reader.process(logImport.getCounter(), inputStream);
            processing(id, jobContext, read, logImport.getCounter(), logImport.getTotal());
        }
    }

    private void processing(String id,
                            JobContext jobContext,
                            CsvewResultReader<CsvUserValueDTO> read,
                            int counter,
                            int total) {
        final JobDashboardProgressBar progressBar;
        if (counter == 0) {
            progressBar = jobContext.progressBar(total);
        } else {
            //reset
            logImportService.updateCounterAndProgressById(id, counter, 0);
            log.info("COUNTER={} AND TOTAL={}", counter, total);
            progressBar = jobContext.progressBar(counter);
            logImportService.updateCounterAndProgressById(id, counter, getProgress(counter, total));
        }

        try {
            for (CsvUserValueDTO csvUserValueDTO : read.getValues()) {
                Thread.sleep(15000);
                progressBar.setValue(csvUserValueDTO.getLine());
                logImportService.updateCounterAndProgressById(id, csvUserValueDTO.getLine(), getProgress(csvUserValueDTO.getLine(), total));
                log.info("LINE={}", csvUserValueDTO.getLine());
            }
            logImportService.updateStatusAndMessageById(id, LogImportStatus.COMPLETED, "Done");
        } catch (InterruptedException ite) {
            Thread.currentThread().interrupt();
        }

    }

    public static int getProgress(int counter, int total) {
        return Math.round(100 * counter / total);
    }
}
