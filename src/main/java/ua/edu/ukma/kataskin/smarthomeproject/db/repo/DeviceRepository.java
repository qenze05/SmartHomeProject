package ua.edu.ukma.kataskin.smarthomeproject.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity.DeviceType;
import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    List<DeviceEntity> findByType(DeviceType type);
    List<DeviceEntity> findByRoom_Id(Long roomId);
}