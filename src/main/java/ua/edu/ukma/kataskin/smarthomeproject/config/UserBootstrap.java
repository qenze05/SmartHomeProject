package ua.edu.ukma.kataskin.smarthomeproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.UserEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.user.UserRepository;

@Configuration
public class UserBootstrap {

    @Bean
    CommandLineRunner initUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByNameIgnoreCase("admin").isEmpty()) {
                UserEntity u = new UserEntity();
                u.setName("admin");
                u.setPasswordHash(encoder.encode("admin"));
                repo.save(u);
            }
        };
    }
}

