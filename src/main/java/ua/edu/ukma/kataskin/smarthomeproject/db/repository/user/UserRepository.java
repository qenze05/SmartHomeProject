package ua.edu.ukma.kataskin.smarthomeproject.db.repository.user;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.edu.ukma.kataskin.smarthomeproject.db.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByNameIgnoreCase(String name);
}
