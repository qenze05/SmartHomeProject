package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherResponse;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirConditionerLogicTest {

    @Mock
    private WeatherService weatherService;

    private DefaultAirConditionerService airConditionerService;

    @BeforeEach
    void setUp() {
        airConditionerService = new DefaultAirConditionerService(weatherService);
    }

    @Test
    void testAutoAdjustCooling() {
        // Arrange - Hot outdoor temperature (> 26°C)
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = new WeatherResponse.Main();
        weatherResponse.main.temp = 30.0; // 30°C outdoor

        when(weatherService.getCurrentWeather()).thenReturn(weatherResponse);

        AirConditionerDeviceDTO device = new AirConditionerDeviceDTO(
                UUID.randomUUID(),
                DeviceType.AIR_CONDITIONER,
                "Test AC");
        device.temperature = 25.0;
        device.powerPercent = 0.0;

        // Act
        AirConditionerDeviceDTO result = airConditionerService.autoAdjust(device);

        // Assert - Temperature should decrease for cooling (outdoor 30°C -> target
        // ~24°C)
        assertThat(result.temperature).isLessThan(30.0);
        assertThat(result.temperature).isBetween(23.0, 27.0); // Expected range per getRecommendedTemperature
        assertThat(result.powerPercent).isGreaterThan(0.0); // Power should be activated
    }

    @Test
    void testAutoAdjustHeating() {
        // Arrange - Cold outdoor temperature (< 12°C)
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = new WeatherResponse.Main();
        weatherResponse.main.temp = 5.0; // 5°C outdoor

        when(weatherService.getCurrentWeather()).thenReturn(weatherResponse);

        AirConditionerDeviceDTO device = new AirConditionerDeviceDTO(
                UUID.randomUUID(),
                DeviceType.AIR_CONDITIONER,
                "Test AC");
        device.temperature = 18.0;
        device.powerPercent = 0.0;

        // Act
        AirConditionerDeviceDTO result = airConditionerService.autoAdjust(device);

        // Assert - Temperature should increase for heating (outdoor 5°C -> target
        // ~20-23°C)
        assertThat(result.temperature).isGreaterThan(5.0);
        assertThat(result.temperature).isBetween(20.0, 23.0); // Expected range per getRecommendedTemperature
        assertThat(result.powerPercent).isGreaterThan(0.0); // Power should be activated
    }

    @Test
    void testFilterLogic() {
        // Arrange - High humidity
        AirConditionerDeviceDTO device = new AirConditionerDeviceDTO(
                UUID.randomUUID(),
                DeviceType.AIR_CONDITIONER,
                "Test AC");
        device.temperature = 22.0;
        device.humidityPercent = 70.0; // > 60%, should trigger filter

        // Act
        AirConditionerDeviceDTO result = airConditionerService.setTargetHumidity(device, 70.0);

        // Assert - Filter should be ON when humidity > 60%
        assertThat(result.filterIsOn).isTrue();

        // Arrange - Low humidity
        device.humidityPercent = 50.0; // < 60%, filter should be OFF

        // Act
        result = airConditionerService.setTargetHumidity(device, 50.0);

        // Assert - Filter should be OFF when humidity <= 60%
        assertThat(result.filterIsOn).isFalse();
    }
}
