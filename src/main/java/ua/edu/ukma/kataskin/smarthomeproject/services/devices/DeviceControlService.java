package ua.edu.ukma.kataskin.smarthomeproject.services.devices;

import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.DefaultAirConditionerService;

import java.util.UUID;

public interface DeviceControlService {
    DeviceRepository repo();
    DefaultAirConditionerService climate();
    AirConditionerDevice autoAdjustConditioner(UUID id);
    Device updateDevice(UUID id, Device updated);
    Device createDevice(Device device);
}
