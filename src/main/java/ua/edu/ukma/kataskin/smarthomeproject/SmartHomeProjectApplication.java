package ua.edu.ukma.kataskin.smarthomeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartHomeProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHomeProjectApplication.class, args);
    }

}
