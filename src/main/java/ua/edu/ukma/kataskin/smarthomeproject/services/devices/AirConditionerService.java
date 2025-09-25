package ua.edu.ukma.kataskin.smarthomeproject.services.devices;

import org.springframework.stereotype.Service;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherService;

@Service
public class AirConditionerService {

    private final WeatherService weatherService;

    public AirConditionerService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private double getRecommendedTemperature(double outdoorTemp) {
        if (outdoorTemp >= 26.0) {
            return adjust(outdoorTemp - 6.0, 23.0, 27.0);
        } else if (outdoorTemp <= 12.0) {
            return adjust(outdoorTemp + 6.0, 20.0, 23.0);
        } else {
            return 22.0;
        }
    }

    private double adjust(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public AirConditionerDevice autoAdjust(AirConditionerDevice device) {
        double outdoor = weatherService.getCurrentWeather().main.temp;

        double currentRoomTemp = device.temperature;
        double targetTemp = getRecommendedTemperature(outdoor);

        double delta = Math.abs(currentRoomTemp - targetTemp);
        double power = (delta < 0.5) ? 0.0 : adjust(delta * 15.0, 20.0, 100.0);

        device.temperature = targetTemp;
        device.powerPercent = power;

        Double humidity = device.humidityPercent;
        device.filterIsOn = (humidity != null && humidity > 60.0);

        return device;
    }
}
