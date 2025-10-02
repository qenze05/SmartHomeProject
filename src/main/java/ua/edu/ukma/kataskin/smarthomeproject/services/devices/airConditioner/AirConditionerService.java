package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner;

import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;

public interface AirConditionerService {

    AirConditionerDevice autoAdjust(AirConditionerDevice device);

    AirConditionerDevice setTargetTemperature(AirConditionerDevice device, double celsius);
    AirConditionerDevice increaseTemperature(AirConditionerDevice device, double stepCelsius);
    AirConditionerDevice decreaseTemperature(AirConditionerDevice device, double stepCelsius);

    AirConditionerDevice setTargetHumidity(AirConditionerDevice device, Double humidityPercent);

    AirConditionerDevice setPowerPercent(AirConditionerDevice device, double percent);

    boolean needsFilterOn(AirConditionerDevice device);
    AirConditionerDevice setFilterOn(AirConditionerDevice device, boolean on);
}