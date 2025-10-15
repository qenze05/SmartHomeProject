package ua.edu.ukma.kataskin.smarthomeproject.services.devices;

import ua.edu.ukma.kataskin.smarthomeproject.db.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.DefaultAirConditionerService;

import java.util.List;
import java.util.UUID;

public interface DeviceControlService {
    DeviceRepository repo();
    DefaultAirConditionerService climate();
    AirConditionerDeviceDTO autoAdjustConditioner(UUID id);
    DeviceDTO updateDevice(UUID id, DeviceDTO updated);
    DeviceDTO createDevice(DeviceDTO deviceDTO);

    DeviceDTO getDeviceById(UUID id);
    List<DeviceDTO> getAllDevices();
    DeviceDTO deleteDevice(UUID id);
}
