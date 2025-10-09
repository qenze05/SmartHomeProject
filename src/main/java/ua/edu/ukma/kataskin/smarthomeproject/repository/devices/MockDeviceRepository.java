package ua.edu.ukma.kataskin.smarthomeproject.repository.devices;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.DeviceType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Primary
public class MockDeviceRepository implements DeviceRepository {

    private final ConcurrentMap<UUID, Device> store = new ConcurrentHashMap<>();

    @Override
    public Device get(UUID id) {
        if (id == null) return null;
        return store.get(id);
    }

    @Override
    public List<Device> getAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Device save(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Trying to save null device");
        }

        UUID id = device.id;
        if (id == null) {
            device.id = UUID.randomUUID();
        }
        store.put(id, device);
        return device;
    }

    @Override
    public Device delete(UUID id) {
        if (id == null) return null;
        return store.remove(id);
    }

    @Override
    public boolean exists(UUID id) {
        if (id == null) return false;
        return store.containsKey(id);
    }

    @Override
    public List<Device> getByType(DeviceType type) {
        if (type == null) return List.of();
        return store.values()
                .stream()
                .filter(d -> type.equals(d.deviceType))
                .toList();
    }

    @Override
    public List<Device> getByRoomID(String roomId) {
        if (roomId == null || roomId.isBlank()) return List.of();
        return store.values()
                .stream()
                .filter(d -> roomId.equals(d.roomId))
                .toList();
    }
}

