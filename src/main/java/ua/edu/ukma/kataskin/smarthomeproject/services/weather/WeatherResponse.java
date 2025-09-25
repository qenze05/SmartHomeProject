package ua.edu.ukma.kataskin.smarthomeproject.services.weather;

import java.util.List;

public class WeatherResponse {
    public Main main;
    public List<Weather> weather;
    public String name;

    public static class Main {
        public double temp;
        public int pressure;
        public int humidity;
    }

    public static class Weather {
        public String description;
    }
}
