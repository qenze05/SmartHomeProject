package ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device;

import java.util.UUID;

public class LightsDeviceDTO extends DeviceDTO {
    private boolean isOn;

    public LightsDeviceDTO(UUID id, DeviceType deviceType, String name) {
        super(id, deviceType, name);
        this.isOn = false;
    }
}
