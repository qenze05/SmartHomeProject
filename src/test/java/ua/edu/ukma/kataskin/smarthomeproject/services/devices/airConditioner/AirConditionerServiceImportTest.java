package ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.edu.ukma.kataskin.smarthomeproject.services.weather.WeatherService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {})
@Import(DefaultAirConditionerService.class)
class AirConditionerServiceImportTest {

    @Autowired
    private DefaultAirConditionerService airConditionerService;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void testServiceBeanCreation() {
        assertThat(airConditionerService).isNotNull();
        assertThat(airConditionerService).isInstanceOf(DefaultAirConditionerService.class);
    }
}
