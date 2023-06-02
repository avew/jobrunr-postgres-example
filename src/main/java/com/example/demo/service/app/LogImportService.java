package com.example.demo.service.app;

import com.example.demo.domain.LogImport;
import com.example.demo.domain.enumeration.LogImportCategory;
import com.example.demo.domain.enumeration.LogImportStatus;
import com.example.demo.domain.enumeration.LogImportType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LogImportService {

    LogImport prepare(String companyId,
                      String originalFilename,
                      String filename,
                      long size,
                      String contentType,
                      String checksum,
                      LogImportType type,
                      LogImportCategory category,
                      byte[] bytes);

    LogImport save(LogImport logImport);
    void deleteById(String id);

    void updateStatusAndMessageById(String id, LogImportStatus status, String message);

    void updateTotalById(String id, int total);

    void updateCounterAndProgressById(String id, int counter, int progress);
}
