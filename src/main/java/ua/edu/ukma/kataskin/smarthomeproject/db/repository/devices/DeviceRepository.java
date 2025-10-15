package ua.edu.ukma.kataskin.smarthomeproject.db.repository.devices;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    List<DeviceEntity> findByDeviceType(DeviceType type);
    List<DeviceEntity> findByRoom_Id(Long roomId);
}