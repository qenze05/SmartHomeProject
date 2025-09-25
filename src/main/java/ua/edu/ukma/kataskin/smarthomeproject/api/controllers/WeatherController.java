package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherResponse;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherService;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public WeatherResponse getWeather() {
        return weatherService.getCurrentWeather();
    }
}
