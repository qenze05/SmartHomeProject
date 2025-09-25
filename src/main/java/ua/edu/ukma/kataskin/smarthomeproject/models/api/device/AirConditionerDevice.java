package ua.edu.ukma.kataskin.smarthomeproject.models.api.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AirConditionerDevice extends Device {
    public Double temperature;
    public Double humidityPercent;
    public Double powerPercent;
    public Boolean filterIsOn;

    public AirConditionerDevice(
            UUID id,
            @NotNull(message = "Device type must not be null, use UNSPECIFIED if type is unknown")
            DeviceType deviceType,
            @Size(min = 2, max = 32, message = "Device name must be 2 to 32 characters long")
            String name
    ) {
        super(id, deviceType, name);
        this.temperature = 0.0;
        this.humidityPercent = 0.0;
        this.powerPercent = 0.0;
        this.filterIsOn = false;
    }
}
