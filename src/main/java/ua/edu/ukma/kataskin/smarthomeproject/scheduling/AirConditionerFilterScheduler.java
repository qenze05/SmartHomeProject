package ua.edu.ukma.kataskin.smarthomeproject.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceService;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.AirConditionerService;

import java.util.List;

@Component
public class AirConditionerFilterScheduler {

    private static final Logger log = LoggerFactory.getLogger(AirConditionerFilterScheduler.class);

    private final DeviceService deviceService;
    private final AirConditionerService airConditionerService;

    public AirConditionerFilterScheduler(DeviceService deviceService,
                                         AirConditionerService airConditionerService) {
        this.deviceService = deviceService;
        this.airConditionerService = airConditionerService;
    }

    @Scheduled(fixedRate = 3600000)
    public void checkAirConditionerFilters() {
        log.info("=== Air Conditioner Filter Check ===");

        List<DeviceDTO> allDevices = deviceService.getAllDevices();

        List<AirConditionerDeviceDTO> airConditioners = allDevices.stream()
                .filter(d -> d.deviceType == DeviceType.AIR_CONDITIONER)
                .filter(d -> d instanceof AirConditionerDeviceDTO)
                .map(d -> (AirConditionerDeviceDTO) d)
                .toList();

        if (airConditioners.isEmpty()) {
            log.info("No air conditioner devices found. Skipping filter check.");
            return;
        }

        for (AirConditionerDeviceDTO ac : airConditioners) {
            boolean needsFilter = airConditionerService.needsFilterOn(ac);

            if (needsFilter && !ac.filterIsOn) {
                log.warn("⚠️  Air Conditioner '{}' (ID: {}) needs filter activated! Humidity: {}%",
                        ac.name, ac.id, ac.humidityPercent);
            } else if (needsFilter) {
                log.info("✓ Air Conditioner '{}' (ID: {}) filter is active. Humidity: {}%",
                        ac.name, ac.id, ac.humidityPercent);
            } else {
                log.debug("  Air Conditioner '{}' (ID: {}) filter check: OK", ac.name, ac.id);
            }
        }

        log.info("=== Filter Check Complete ({} devices checked) ===", airConditioners.size());
    }
}
