package ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AirConditionerDeviceDTO extends DeviceDTO {
    public Double temperature;
    public Double humidityPercent;
    public Double powerPercent;
    public Boolean filterIsOn;

    public AirConditionerDeviceDTO(
            UUID id,
            @NotNull(message = DeviceDTO.deviceTypeValidationMessage)
            DeviceType deviceType,
            @Size(min = 2, max = 32, message = DeviceDTO.deviceNameValidationMessage)
            String name
    ) {
        super(id, deviceType, name);
        this.temperature = 0.0;
        this.humidityPercent = 0.0;
        this.powerPercent = 0.0;
        this.filterIsOn = false;
    }
}
