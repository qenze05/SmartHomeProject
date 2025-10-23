package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.AirConditionerDeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceControlService;

import java.net.URI;
import java.util.*;

@Validated
@Tag(name = "Devices", description = "Operations for managing devices")
@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    private final DeviceControlService deviceService;

    public DeviceController(DeviceControlService deviceControlService) {
        this.deviceService = deviceControlService;
    }
    @PostMapping
    public ResponseEntity<DeviceDTO> create(@Valid @RequestBody DeviceDTO body) {
        System.out.println("CREATE DTO: type=" + body.deviceType + ", name=" + body.name);
        DeviceDTO created = deviceService.createDevice(body);
        return ResponseEntity.created(URI.create("/api/devices/" + created.id)).body(created);
    }

    @PostMapping("/{id}/air-conditioner/auto-adjust")
    public ResponseEntity<AirConditionerDeviceDTO> autoAdjust(@PathVariable UUID id) {
        AirConditionerDeviceDTO updated = deviceService.autoAdjustConditioner(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<DeviceDTO> list() {
        log.info("GET /api/devices");
        List<DeviceDTO> items = deviceService.getAllDevices();
        log.info("GET /api/devices → {} item(s)", items.size());
        return items;
    }

    @GetMapping("/{id}")
    public DeviceDTO get(@PathVariable UUID id) {
        DeviceDTO dev = deviceService.getDeviceById(id);
        if (dev == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return dev;
    }

    @PutMapping("/{id}")
    public DeviceDTO update(@PathVariable UUID id, @Valid @RequestBody DeviceDTO body) {
        DeviceDTO existing = deviceService.getDeviceById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return deviceService.updateDevice(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        DeviceDTO removed = deviceService.deleteDevice(id);
        if (removed == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return ResponseEntity.noContent().build();
    }
}
