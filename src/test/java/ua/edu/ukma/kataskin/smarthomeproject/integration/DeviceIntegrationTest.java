package ua.edu.ukma.kataskin.smarthomeproject.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DeviceIntegrationTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void testCreateAndRetrieveDevice() {
        // Arrange
        DeviceDTO deviceToCreate = new DeviceDTO(null, DeviceType.LIGHTS, "Integration Test Device");

        // Act - Create device
        DeviceDTO createdDevice = deviceService.createDevice(deviceToCreate);

        // Assert - Verify creation
        assertThat(createdDevice).isNotNull();
        assertThat(createdDevice.id).isNotNull(); // ID should be generated
        assertThat(createdDevice.name).isEqualTo("Integration Test Device");
        assertThat(createdDevice.deviceType).isEqualTo(DeviceType.LIGHTS);

        // Act - Retrieve device
        DeviceDTO retrievedDevice = deviceService.getDeviceById(createdDevice.id);

        // Assert - Verify retrieval
        assertThat(retrievedDevice).isNotNull();
        assertThat(retrievedDevice.id).isEqualTo(createdDevice.id);
        assertThat(retrievedDevice.name).isEqualTo("Integration Test Device");
        assertThat(retrievedDevice.deviceType).isEqualTo(DeviceType.LIGHTS);

        // Verify it exists in repository
        assertThat(deviceRepository.findById(createdDevice.id)).isPresent();
    }
}
