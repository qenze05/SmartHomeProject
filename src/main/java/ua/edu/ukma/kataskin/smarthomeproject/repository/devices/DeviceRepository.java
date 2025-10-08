package ua.edu.ukma.kataskin.smarthomeproject.repository.devices;

import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.DeviceType;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository {

    Device get(UUID id);

    List<Device> getAll();

    Device save(Device device);

    Device delete(UUID id);

    boolean exists(UUID id);

    List<Device> getByType(DeviceType type);

    List<Device> getByRoomID(String roomId);
}
