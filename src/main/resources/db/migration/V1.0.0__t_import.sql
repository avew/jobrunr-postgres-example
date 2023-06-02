-- begin
CREATE TABLE t_log_import
(
    id                varchar(37)  NOT NULL,
    company_id        varchar(37)           DEFAULT NULL,

    status            varchar(37)  NOT NULL,
    message           text,

    type              varchar(100) NOT NULL,
    category          varchar(100) NOT NULL,
    content_type      varchar(100) NOT NULL,
    original_filename text         NOT NULL,
    filename          text         NOT NULL,
    size              int4         NOT NULL,
    checksum          varchar(255) NOT NULL,
    job_id            varchar(255) NULL     DEFAULT NULL,

    file_base         bytea        NULL     DEFAULT NULL,

    counter           int4         NOT NULL DEFAULT 0,
    total             int4         NOT NULL DEFAULT 0,
    progress          int4         NOT NULL DEFAULT 0,


    CONSTRAINT t_log_import_pkey PRIMARY KEY (id)
);
-- end 

CREATE TABLE t_log_import_validation
(
    id            varchar(50) NOT NULL,
    line          int4        NOT NULL,
    message       text,
    log_import_id varchar(50) NOT NULL DEFAULT NULL,
    error         bool                 DEFAULT false,
    CONSTRAINT t_log_import_validation_pkey PRIMARY KEY (id),
    CONSTRAINT fk_t_log_import_validation_id FOREIGN KEY (log_import_id) REFERENCES t_log_import (id) ON DELETE CASCADE ON UPDATE NO ACTION
);
