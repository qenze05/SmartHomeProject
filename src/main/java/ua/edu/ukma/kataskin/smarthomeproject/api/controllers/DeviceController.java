package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WrongDeviceTypeException;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.DeviceControlService;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.airConditioner.DefaultAirConditionerService;

import java.net.URI;
import java.util.*;

@Validated
@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceControlService deviceService;

    public DeviceController(DeviceControlService deviceControlService) {
        this.deviceService = deviceControlService;
    }

    @PostMapping
    public ResponseEntity<Device> create(@Valid @RequestBody Device body) {
        Device created = deviceService.createDevice(body);
        return ResponseEntity.created(URI.create("/api/devices/" + created.id)).body(created);
    }

    @PostMapping("/{id}/air-conditioner/auto-adjust")
    public ResponseEntity<AirConditionerDevice> autoAdjust(@PathVariable UUID id) {
        AirConditionerDevice updated = deviceService.autoAdjustConditioner(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Device> list() {
        return deviceService.repo().getAll();
    }

    @GetMapping("/{id}")
    public Device get(@PathVariable UUID id) {
        Device dev = deviceService.repo().get(id);
        if (dev == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return dev;
    }

    @PutMapping("/{id}")
    public Device update(@PathVariable UUID id, @Valid @RequestBody Device body) {
        Device existing = deviceService.repo().get(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return deviceService.updateDevice(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Device removed = deviceService.repo().delete(id);
        if (removed == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return ResponseEntity.noContent().build();
    }
}
