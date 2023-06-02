package com.example.demo.service.csv;


import lombok.*;
import org.jobrunr.jobs.lambdas.JobRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomJobRequest implements JobRequest {

    private String id;

    @Override
    public Class<JobCsv> getJobRequestHandler() {
        return JobCsv.class;
    }
}
