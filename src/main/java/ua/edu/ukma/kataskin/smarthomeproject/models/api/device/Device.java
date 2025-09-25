package ua.edu.ukma.kataskin.smarthomeproject.models.api.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Device {
    public UUID id;
    public DeviceType deviceType;
    public String name;

    public Device(
            UUID id,
            @NotNull(message = "Device type must not be null, use UNSPECIFIED if type is unknown")
            DeviceType deviceType,
            @Size(min = 2, max = 32, message = "Device name must be 2 to 32 characters long")
            String name
    ) {
        this.id = id;
        this.deviceType = deviceType;
        this.name = name;
    }

}
