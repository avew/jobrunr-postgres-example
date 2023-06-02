package com.example.demo.service.csv;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomJobRequest {

    private String id;

//    @Override
//    public Class<JobCsvRequestHandler> getJobRequestHandler() {
//        return JobCsvRequestHandler.class;
//    }
}
