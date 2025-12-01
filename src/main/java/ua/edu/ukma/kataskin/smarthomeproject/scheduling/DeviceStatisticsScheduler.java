package ua.edu.ukma.kataskin.smarthomeproject.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DeviceStatisticsScheduler {

    private static final Logger log = LoggerFactory.getLogger(DeviceStatisticsScheduler.class);

    private final DeviceService deviceService;

    public DeviceStatisticsScheduler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void aggregateDeviceStatistics() {
        log.info("=== Daily Device Statistics Aggregation ===");

        List<DeviceDTO> allDevices = deviceService.getAllDevices();

        Map<DeviceType, Long> devicesByType = allDevices.stream()
                .collect(Collectors.groupingBy(d -> d.deviceType, Collectors.counting()));

        log.info("Total devices: {}", allDevices.size());
        devicesByType.forEach((type, count) ->
                log.info("  {} devices: {}", type, count)
        );

        log.info("=== End of Statistics ===");
    }
}
