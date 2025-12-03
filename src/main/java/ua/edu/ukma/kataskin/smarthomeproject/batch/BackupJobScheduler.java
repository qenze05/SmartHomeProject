package ua.edu.ukma.kataskin.smarthomeproject.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackupJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job smartHomeBackupJob;

    public BackupJobScheduler(JobLauncher jobLauncher,
                              Job smartHomeBackupJob) {
        this.jobLauncher = jobLauncher;
        this.smartHomeBackupJob = smartHomeBackupJob;
    }

    // 03:00
    @Scheduled(cron = "0 0 3 * * *")
    public void runNightlyBackup() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("[SCHEDULER] Starting nightly smartHomeBackupJob");
        jobLauncher.run(smartHomeBackupJob, params);
    }
}
