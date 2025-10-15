package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner;

import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;

public interface AirConditionerService {

    AirConditionerDeviceDTO autoAdjust(AirConditionerDeviceDTO device);

    AirConditionerDeviceDTO setTargetTemperature(AirConditionerDeviceDTO device, double celsius);
    AirConditionerDeviceDTO increaseTemperature(AirConditionerDeviceDTO device, double stepCelsius);
    AirConditionerDeviceDTO decreaseTemperature(AirConditionerDeviceDTO device, double stepCelsius);

    AirConditionerDeviceDTO setTargetHumidity(AirConditionerDeviceDTO device, Double humidityPercent);

    AirConditionerDeviceDTO setPowerPercent(AirConditionerDeviceDTO device, double percent);

    boolean needsFilterOn(AirConditionerDeviceDTO device);
    AirConditionerDeviceDTO setFilterOn(AirConditionerDeviceDTO device, boolean on);
}