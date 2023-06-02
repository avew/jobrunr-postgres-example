package com.example.demo.repository;


import com.example.demo.domain.LogImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogImportRepository extends JpaRepository<LogImport, String>, JpaSpecificationExecutor<LogImport> {

    Optional<LogImport> findByChecksum(String cheksum);

    @Modifying(flushAutomatically = true)
    @Query("update LogImport set total = ?2 where id=?1")
    void updateTotalById(String id, int total);

//	@Modifying(flushAutomatically = true)
//	@Query("update LogImport set counter = ?2,progress = ?3 where id=?1")
//	void updateProgressById(String id, int count, double progress);
//
//	@Query("SELECT new com.pajakku.app.response.bp.LogFileIdAndName(log.id, log.originalFilename) " +
//		"FROM LogImport log " +
//		"WHERE log.id IN (?1)")
//	List<LogFileIdAndName> findAllLogFilesByIdIn(List<String> refFileIds);

}
