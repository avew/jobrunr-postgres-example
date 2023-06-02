package com.example.demo.service.csv;

import com.example.demo.domain.LogImport;
import com.example.demo.domain.enumeration.LogImportStatus;
import com.example.demo.repository.LogImportRepository;
import com.example.demo.service.app.LogImportService;
import com.google.common.io.ByteSource;
import io.github.avew.CsvResultReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCsv implements JobRequestHandler<CustomJobRequest> {

    private final LogImportService logImportService;
    private final LogImportRepository repository;

    @Override
    public void run(CustomJobRequest customJobRequest) throws IOException {
        LogImport logImport = repository.findById(customJobRequest.getId()).get();
        CsvUserReader reader = new CsvUserReader();
        InputStream inputStream = ByteSource.wrap(Base64.getDecoder().decode(logImport.getBase64())).openStream();
        if (logImport.getCounter() == 0) {
            CsvResultReader<CsvUserValueDTO> read = reader.read(inputStream);
            int total = read.getCount();
            if (!read.isError()) {
                logImportService.updateTotalById(customJobRequest.getId(), total);
                processing(customJobRequest, read, 0, total);
            }
        } else {
            CsvResultReader<CsvUserValueDTO> read = reader.read(logImport.getCounter(), inputStream);
            processing(customJobRequest, read, logImport.getCounter(), logImport.getTotal());
        }
    }

    private void processing(CustomJobRequest customJobRequest, CsvResultReader<CsvUserValueDTO> read, int counter, int total) {
        JobDashboardProgressBar progressBar;
        if (counter == 0) {
            progressBar = jobContext().progressBar(total);
        } else {
            //reset
            logImportService.updateCounterAndProgressById(customJobRequest.getId(), counter, 0);
            log.info("COUNTER={} AND TOTAL={}", counter, total);
            progressBar = jobContext().progressBar(counter);
            logImportService.updateCounterAndProgressById(customJobRequest.getId(), counter, getProgress(counter, total));
        }

        for (CsvUserValueDTO csvUserValueDTO : read.getValues()) {
            progressBar.setValue(csvUserValueDTO.getLine());
            logImportService.updateCounterAndProgressById(customJobRequest.getId(), csvUserValueDTO.getLine(), getProgress(csvUserValueDTO.getLine(), total));
            log.info("LINE={}", csvUserValueDTO.getLine());
        }
        logImportService.updateStatusAndMessageById(customJobRequest.getId(), LogImportStatus.COMPLETED, "Done");
    }

    public static int getProgress(int counter, int total) {
        return Math.round(100 * counter / total);
    }
}
