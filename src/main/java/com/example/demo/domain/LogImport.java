package com.example.demo.domain;

import com.example.demo.domain.enumeration.LogImportCategory;
import com.example.demo.domain.enumeration.LogImportStatus;
import com.example.demo.domain.enumeration.LogImportType;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "t_log_import")
@Cache(region = "log_import", usage = CacheConcurrencyStrategy.READ_WRITE)
public class LogImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "company_id")
    private String companyId;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LogImportStatus status;

    @NotNull
    @Column(name = "message")
    private String message;

    //PPH PPN MASTER
    @NotNull
    @Enumerated(EnumType.STRING)
    private LogImportType type;

    //LT_21
    @NotNull
    @Enumerated(EnumType.STRING)
    private LogImportCategory category;

    private String originalFilename;

    private String filename;

    @Builder.Default
    private long size = 0;

    private String checksum;

    @Builder.Default
    private int counter = 0;

    @Builder.Default
    private int total = 0;

    @Builder.Default
    private double progress = 0L;

    @NotNull
    @Column(name = "content_type")
    private String contentType;

    @Column(name = "job_id")
    private String jobId;

    @Lob
    @Column(name = "file_base")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] base64;


}

