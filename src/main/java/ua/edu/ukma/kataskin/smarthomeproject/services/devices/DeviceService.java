package ua.edu.ukma.kataskin.smarthomeproject.services.devices;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WrongDeviceTypeException;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.DefaultAirConditionerService;

import java.util.Objects;
import java.util.UUID;

@Service
@Primary
public class DeviceService implements DeviceControlService {

    private final DeviceRepository deviceRepository;
    private final DefaultAirConditionerService airConditionerService;

    public DeviceService(DeviceRepository deviceRepository,
                         DefaultAirConditionerService airConditionerService) {
        this.deviceRepository = deviceRepository;
        this.airConditionerService = airConditionerService;
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
    public AirConditionerDevice autoAdjustConditioner(UUID id) {
        Device base = repo().get(id);

        if (base == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }

        if (!(base instanceof AirConditionerDevice conditionerDevice)) {
            throw new WrongDeviceTypeException("Device is not an air conditioner");
        }

        AirConditionerDevice updated = climate().autoAdjust(conditionerDevice);
        repo().save(updated);
        return updated;
    }

    @Override
    public Device updateDevice(UUID id, Device updated) {
        Device updatedDevice = new Device(id, updated.deviceType, updated.name);
        repo().save(updatedDevice);
        return updatedDevice;
    }

    @Override
    public Device createDevice(Device device) {
        UUID id = UUID.randomUUID();
        Device created;

        //TODO: - Add factory initializer
        if (Objects.requireNonNull(device.deviceType) == DeviceType.AIR_CONDITIONER) {
            created = new AirConditionerDevice(id, device.deviceType, device.name);
        } else {
            created = new Device(id, device.deviceType, device.name);
        }

        repo().save(created);
        return created;
    }


}
