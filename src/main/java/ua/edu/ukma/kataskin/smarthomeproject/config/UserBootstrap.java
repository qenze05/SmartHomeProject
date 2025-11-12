package ua.edu.ukma.kataskin.smarthomeproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.UserEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.user.UserRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.user.UserRole;

@Configuration
public class UserBootstrap {

    @Bean
    CommandLineRunner initUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            var name = "admin2";
            if (repo.findByNameIgnoreCase(name).isEmpty()) {
                UserEntity u = new UserEntity();
                u.setName(name);
                u.setRole(UserRole.OWNER);
                u.setPasswordHash(encoder.encode(name));
                repo.save(u);
            }
        };
    }
}

