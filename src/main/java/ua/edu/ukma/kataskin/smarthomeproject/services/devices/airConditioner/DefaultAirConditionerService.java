package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.filterPolicy.FilterPolicy;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherService;

@Service
@Primary
public class DefaultAirConditionerService implements AirConditionerService {

    private static final double MIN_TEMP = 16.0;
    private static final double MAX_TEMP = 30.0;
    private static final double MIN_POWER = 0.0;
    private static final double MAX_POWER = 100.0;
    private static final double MIN_HUMIDITY = 20.0;
    private static final double MAX_HUMIDITY = 80.0;

    private final WeatherService weatherService;
    private FilterPolicy filterPolicy;

    public DefaultAirConditionerService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Autowired(required = false)
    public void setFilterPolicy(FilterPolicy filterPolicy) {
        this.filterPolicy = filterPolicy;
    }

    @Override
    public AirConditionerDevice autoAdjust(AirConditionerDevice device) {
        validateDevice(device);

        double outdoor = weatherService.getCurrentWeather().main.temp;
        double targetTemp = getRecommendedTemperature(outdoor);

        double delta = Math.abs(device.temperature - targetTemp);
        double power = (delta < 0.5) ? 0.0 : clamp(delta * 15.0, 20.0, 100.0);

        device.temperature = clamp(targetTemp, MIN_TEMP, MAX_TEMP);
        device.powerPercent = clamp(power, MIN_POWER, MAX_POWER);

        device.filterIsOn = needsFilterOn(device);

        return device;
    }

    @Override
    public AirConditionerDevice setTargetTemperature(AirConditionerDevice device, double celsius) {
        validateDevice(device);
        device.temperature = clamp(celsius, MIN_TEMP, MAX_TEMP);
        return device;
    }

    @Override
    public AirConditionerDevice increaseTemperature(AirConditionerDevice device, double stepCelsius) {
        validateDevice(device);
        double next = device.temperature + Math.max(0.1, stepCelsius);
        device.temperature = clamp(next, MIN_TEMP, MAX_TEMP);
        return device;
    }

    @Override
    public AirConditionerDevice decreaseTemperature(AirConditionerDevice device, double stepCelsius) {
        validateDevice(device);
        double next = device.temperature - Math.max(0.1, stepCelsius);
        device.temperature = clamp(next, MIN_TEMP, MAX_TEMP);
        return device;
    }

    @Override
    public AirConditionerDevice setTargetHumidity(AirConditionerDevice device, Double humidityPercent) {
        validateDevice(device);
        if (humidityPercent == null) {
            device.humidityPercent = null;
        } else {
            device.humidityPercent = clamp(humidityPercent, MIN_HUMIDITY, MAX_HUMIDITY);
        }

        device.filterIsOn = needsFilterOn(device);
        return device;
    }

    @Override
    public AirConditionerDevice setPowerPercent(AirConditionerDevice device, double percent) {
        validateDevice(device);
        device.powerPercent = clamp(percent, MIN_POWER, MAX_POWER);
        return device;
    }

    @Override
    public boolean needsFilterOn(AirConditionerDevice d) {
        if (filterPolicy != null) {
            return filterPolicy.needsFilterOn(d.humidityPercent);
        }
        return d.humidityPercent != null && d.humidityPercent > 60.0;
    }

    @Override
    public AirConditionerDevice setFilterOn(AirConditionerDevice device, boolean on) {
        validateDevice(device);
        device.filterIsOn = on;
        return device;
    }

    private void validateDevice(AirConditionerDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("AirConditionerDevice must not be null");
        }
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private double getRecommendedTemperature(double outdoorTemp) {
        if (outdoorTemp >= 26.0) {
            return clamp(outdoorTemp - 6.0, 23.0, 27.0);
        } else if (outdoorTemp <= 12.0) {
            return clamp(outdoorTemp + 6.0, 20.0, 23.0);
        } else {
            return 22.0;
        }
    }
}
