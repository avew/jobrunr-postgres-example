package com.example.demo;

import com.example.demo.service.csv.CsvUserReader;
import com.example.demo.service.csv.CsvUserValueDTO;
import com.example.demo.service.csv.CustomJobRequest;
import com.example.demo.service.csv.JobCsv;
import io.github.avew.CsvResultReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

