package com.jimmy.batch.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Component
@EnableScheduling
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job billJob;

    @Scheduled(cron = "0 * * * * ?")
    public void runJob() {
        try {
            jobLauncher.run(billJob, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}