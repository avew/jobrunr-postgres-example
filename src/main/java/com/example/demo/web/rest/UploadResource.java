package com.example.demo.web.rest;

import com.example.demo.domain.LogImport;
import com.example.demo.domain.enumeration.LogImportCategory;
import com.example.demo.domain.enumeration.LogImportType;
import com.example.demo.service.app.LogImportService;
import com.example.demo.service.csv.CustomJobRequest;
import com.example.demo.service.csv.JobCsv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadResource {

    private final JobScheduler jobScheduler;
    private final LogImportService logImportService;

    @PostMapping
    public LogImport upload(
            @RequestParam(required = false) String companyId,
            @RequestParam String type,
            @RequestParam String category,
            @RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        /* clean original filename */
        String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        /* lower case filename */
        originalFilename = originalFilename.toLowerCase().replaceAll("-", "");
        /* extension of file */
        String extension = FilenameUtils.getExtension(originalFilename);
        /* replace original filename with uuid */
        String filename = UUID.randomUUID() + "." + extension;
        /* file size */
        long fileSize = multipartFile.getSize();
        /* checksum */
        String checksum = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
        /* save to log preparation async */
        LogImport logImport = logImportService.prepare(companyId,
                originalFilename,
                filename,
                fileSize,
                extension,
                checksum,
                LogImportType.valueOf(type),
                LogImportCategory.valueOf(category),
                multipartFile.getBytes());
        JobId enqueue = jobScheduler.<JobCsv>enqueue(jobCsv -> jobCsv.run(CustomJobRequest.builder().id(logImport.getId()).build()));
        logImport.setJobId(enqueue.asUUID().toString());
        return logImportService.save(logImport);
    }
}
