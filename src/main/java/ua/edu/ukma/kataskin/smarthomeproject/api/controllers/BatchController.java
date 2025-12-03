package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job smartHomeBackupJob;

    public BatchController(JobLauncher jobLauncher,
                           Job smartHomeBackupJob) {
        this.jobLauncher = jobLauncher;
        this.smartHomeBackupJob = smartHomeBackupJob;
    }

    @PostMapping("/backup")
    public ResponseEntity<String> runBackupJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(smartHomeBackupJob, params);
        return ResponseEntity.ok("Backup job started");
    }
}
