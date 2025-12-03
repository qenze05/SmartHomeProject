package ua.edu.ukma.kataskin.smarthomeproject.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.user.UserRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.devices.DeviceRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SmartHomeBackupJobConfig {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public SmartHomeBackupJobConfig(UserRepository userRepository,
                                    DeviceRepository deviceRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    private String ts() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    @Bean
    public Job backupJob(JobRepository repo,
                         Step backupUsers,
                         Step backupDevices) {

        return new JobBuilder("backupJob", repo)
                .start(backupUsers)
                .next(backupDevices)
                .build();
    }

    @Bean
    public Step backupUsers(JobRepository repo,
                            PlatformTransactionManager tx) {
        Tasklet tasklet = (contribution, context) -> {

            Files.createDirectories(Path.of("backup"));
            Path file = Path.of("backup", "users-" + ts() + ".json");

            var users = userRepository.findAll();
            Files.writeString(file, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users));

            return org.springframework.batch.repeat.RepeatStatus.FINISHED;
        };

        return new StepBuilder("backupUsers", repo)
                .tasklet(tasklet, tx)
                .build();
    }

    @Bean
    public Step backupDevices(JobRepository repo,
                              PlatformTransactionManager tx) {
        Tasklet tasklet = (contribution, context) -> {

            Files.createDirectories(Path.of("backup"));
            Path file = Path.of("backup", "devices-" + ts() + ".json");

            var devices = deviceRepository.findAll();
            Files.writeString(file, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(devices));

            return org.springframework.batch.repeat.RepeatStatus.FINISHED;
        };

        return new StepBuilder("backupDevices", repo)
                .tasklet(tasklet, tx)
                .build();
    }
}
