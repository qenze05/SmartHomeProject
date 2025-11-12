package ua.edu.ukma.kataskin.smarthomeproject.web.forms;

import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;

import java.util.UUID;

public class DeviceForm {
    private UUID id;
    private String name;
    private DeviceType deviceType;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DeviceType getDeviceType() { return deviceType; }
    public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }
}

