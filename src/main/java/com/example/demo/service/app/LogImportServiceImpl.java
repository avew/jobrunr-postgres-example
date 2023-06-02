package com.example.demo.service.app;

import com.example.demo.domain.LogImport;
import com.example.demo.domain.enumeration.LogImportCategory;
import com.example.demo.domain.enumeration.LogImportStatus;
import com.example.demo.domain.enumeration.LogImportType;
import com.example.demo.repository.LogImportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static com.example.demo.domain.enumeration.LogImportStatus.UPLOADING;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogImportServiceImpl implements LogImportService {

    private final LogImportRepository repository;

    @Override
    public LogImport prepare(String companyId,
                             String originalFilename,
                             String filename,
                             long size,
                             String contentType,
                             String checksum,
                             LogImportType type,
                             LogImportCategory category,
                             byte[] bytes) {
        return repository
                .save(LogImport.builder()
                        .companyId(companyId)
                        .status(UPLOADING)
                        .message("uploading.message")
                        .originalFilename(originalFilename)
                        .filename(filename)
                        .size(size)
                        .contentType(contentType)
                        .checksum(checksum)
                        .type(type)
                        .category(category)
                        .counter(0)
                        .total(0)
                        .progress(0)
                        .base64(Base64.getEncoder().encode(bytes))
                        .build());
    }

    @Override
    public LogImport save(LogImport logImport) {
        return repository.save(logImport);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public void updateStatusAndMessageById(String id, LogImportStatus status, String message) {
        LogImport logImport = repository.findById(id).get();
        logImport.setStatus(status);
        logImport.setMessage(message);
        repository.save(logImport);
    }

    @Override
    public void updateTotalById(String id, int total) {
        LogImport logImport = repository.findById(id).get();
        logImport.setTotal(total);
        repository.save(logImport);
    }

    @Override
    public void updateCounterAndProgressById(String id, int counter, int progress) {
        LogImport logImport = repository.findById(id).get();
        logImport.setCounter(counter);
        logImport.setProgress(progress);
        repository.save(logImport);
    }
}
