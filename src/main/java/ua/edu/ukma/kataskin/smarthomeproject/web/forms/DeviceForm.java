package ua.edu.ukma.kataskin.smarthomeproject.web.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;

import java.util.UUID;

public class DeviceForm {
    private UUID id;

    @NotBlank(message = "Назва не може бути порожньою")
    @Size(min = 2, max = 32, message = "Назва має бути від 2 до 32 символів")
    private String name;

    @NotNull(message = "Оберіть тип пристрою")
    private DeviceType deviceType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
