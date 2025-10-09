package ua.edu.ukma.kataskin.smarthomeproject.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {}