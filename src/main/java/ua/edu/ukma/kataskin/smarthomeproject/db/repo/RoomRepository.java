package ua.edu.ukma.kataskin.smarthomeproject.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.RoomEntity;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}