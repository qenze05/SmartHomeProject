package ua.edu.ukma.kataskin.smarthomeproject.services.weather;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WeatherApiErrorException;
import ua.edu.ukma.kataskin.smarthomeproject.config.WeatherConfig;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final WeatherConfig config;

    public WeatherService(RestTemplate restTemplate, WeatherConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public WeatherResponse getCurrentWeather() {
        String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                config.getApiUrl(), config.getLat(), config.getLon(), config.getApiKey());
        WeatherResponse res = restTemplate.getForObject(url, WeatherResponse.class);
        if (res == null) {
            throw new WeatherApiErrorException("Weather API returned no data");
        }
        return res;
    }
}

