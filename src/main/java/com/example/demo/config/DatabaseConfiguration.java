package com.example.demo.config;

/*
 * Developed by Asep Rojali on 12/18/18 7:25 PM
 * Last modified 12/18/18 6:00 PM
 * Copyright (c) 2018. All rights reserved.
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

@SuppressWarnings("SpellCheckingInspection")
@Configuration
@EntityScan("com.example.demo.domain")
@EnableJpaRepositories(
		basePackages = "com.example.demo.repository")
@EnableJpaAuditing(
		dateTimeProviderRef = "utcDateTimeProvider")
@EnableTransactionManagement
@Slf4j
public class DatabaseConfiguration {

	@Bean
	public JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}

	@Bean
	public DateTimeProvider utcDateTimeProvider() {
		return () -> Optional.of(Instant.now().atZone(ZoneOffset.UTC));
	}
}