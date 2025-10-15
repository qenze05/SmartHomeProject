package ua.edu.ukma.kataskin.smarthomeproject.services.devices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WrongDeviceTypeException;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.mappers.DeviceMapper;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.groups.GroupRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.room.RoomRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.DefaultAirConditionerService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class DeviceService implements DeviceControlService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final DeviceMapper deviceMapper;

    @Autowired
    private DefaultAirConditionerService airConditionerService;

    public DeviceService(
            DeviceRepository deviceRepository,
            RoomRepository roomRepository,
            GroupRepository groupRepository,
            DeviceMapper deviceMapper
    ) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
        this.groupRepository = groupRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceRepository repo() {
        return deviceRepository;
    }

    @Override
    public DefaultAirConditionerService climate() {
        return airConditionerService;
    }

    @Override
    public AirConditionerDeviceDTO autoAdjustConditioner(UUID id) {
        Optional<DeviceEntity> entity = repo().findById(id);
        if (entity.isEmpty()) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }

        DeviceDTO base = deviceMapper.toDto(entity.get());

        if (base == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }

        if (!(base instanceof AirConditionerDeviceDTO conditionerDevice)) {
            throw new WrongDeviceTypeException("Device is not an air conditioner");
        }

        AirConditionerDeviceDTO updated = climate().autoAdjust(conditionerDevice);
        repo().save(deviceMapper.toEntity(updated, roomRepository, groupRepository));
        return updated;
    }

    @Override
    public DeviceDTO updateDevice(UUID id, DeviceDTO updated) {
        DeviceDTO updatedDeviceDTO = new DeviceDTO(id, updated.deviceType, updated.name);
        repo().save(deviceMapper.toEntity(updatedDeviceDTO, roomRepository, groupRepository));
        return updatedDeviceDTO;
    }

    @Override
    public DeviceDTO createDevice(DeviceDTO deviceDTO) {
        UUID id = UUID.randomUUID();
        DeviceDTO created;

        //TODO: - Add factory initializer
        if (Objects.requireNonNull(deviceDTO.deviceType) == DeviceType.AIR_CONDITIONER) {
            created = new AirConditionerDeviceDTO(id, deviceDTO.deviceType, deviceDTO.name);
        } else {
            created = new DeviceDTO(id, deviceDTO.deviceType, deviceDTO.name);
        }

        repo().save(deviceMapper.toEntity(created, roomRepository, groupRepository));
        return created;
    }

    @Override
    public DeviceDTO getDeviceById(UUID id) {
        Optional<DeviceEntity> entity = repo().findById(id);
        if (entity.isEmpty()) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }

        return deviceMapper.toDto(entity.get());
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        List<DeviceEntity> entities = repo().findAll();
        return entities.stream().map(deviceMapper::toDto).toList();
    }

    @Override
    public DeviceDTO deleteDevice(UUID id) {
        DeviceDTO dto = getDeviceById(id);
        repo().deleteById(id);
        return dto;
    }
}
