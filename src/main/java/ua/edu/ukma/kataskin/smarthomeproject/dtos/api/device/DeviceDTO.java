package ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class DeviceDTO {
    public static final String deviceTypeValidationMessage = "Device type must not be null, use UNSPECIFIED if type is unknown";
    public static final String deviceNameValidationMessage = "Device name must be 2 to 32 characters long";

    public UUID id;
    @NotNull(message = deviceTypeValidationMessage)
    public DeviceType deviceType;
    @Size(min = 2, max = 32, message = deviceNameValidationMessage)
    public String name;
    public String roomId;
    public List<String> groupIds;

    public DeviceDTO() { }
    public DeviceDTO(
            UUID id,
            @NotNull(message = deviceTypeValidationMessage)
            DeviceType deviceType,
            @Size(min = 2, max = 32, message = deviceNameValidationMessage)
            String name
    ) {
        this.id = id;
        this.deviceType = deviceType;
        this.name = name;
        this.roomId = null;
        this.groupIds = null;
    }
}
