package ua.edu.ukma.kataskin.smarthomeproject.models.api.device;

import java.util.UUID;

public class LightsDevice extends Device{
    private boolean isOn;

    public LightsDevice(UUID id, DeviceType deviceType, String name) {
        super(id, deviceType, name);
        this.isOn = false;
    }
}
