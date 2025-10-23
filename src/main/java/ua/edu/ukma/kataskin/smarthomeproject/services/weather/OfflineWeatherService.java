package ua.edu.ukma.kataskin.smarthomeproject.services.weather;

import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
@ConditionalOnProperty(prefix = "openweathermap", name = "enabled", havingValue = "false")
public class OfflineWeatherService implements WeatherService {

    @Override
    public WeatherResponse getCurrentWeather() {
        WeatherResponse res = new WeatherResponse();
        res.name = "Offline";
        WeatherResponse.Main main = new WeatherResponse.Main();
        main.temp = 22;
        main.humidity = 50;
        res.main = main;
        return res;
    }
}
