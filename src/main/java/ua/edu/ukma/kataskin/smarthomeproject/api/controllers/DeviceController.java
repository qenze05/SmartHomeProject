package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WrongDeviceTypeException;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.AirConditionerDevice;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.Device;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.device.DeviceType;
import ua.edu.ukma.kataskin.smarthomeproject.repository.devices.DeviceRepository;
import ua.edu.ukma.kataskin.smarthomeproject.services.devices.AirConditionerService;

import java.net.URI;
import java.util.*;

@Validated
@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final Map<UUID, Device> store = new HashMap<>();

    private final AirConditionerService airConditionerService;

    private final DeviceRepository deviceRepository;

    public DeviceController(AirConditionerService airConditionerService, DeviceRepository deviceRepository) {
        this.airConditionerService = airConditionerService;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping
    public ResponseEntity<Device> create(@Valid @RequestBody Device body) {
        UUID id = UUID.randomUUID();
        Device created;

        //TODO: - Add factory initializer
        if (Objects.requireNonNull(body.deviceType) == DeviceType.AIR_CONDITIONER) {
            created = new AirConditionerDevice(id, body.deviceType, body.name);
        } else {
            created = new Device(id, body.deviceType, body.name);
        }

        deviceRepository.save(created);
        return ResponseEntity.created(URI.create("/api/devices/" + id)).body(created);
    }

    @PostMapping("/{id}/air-conditioner/auto-adjust")
    public ResponseEntity<AirConditionerDevice> autoAdjust(@PathVariable UUID id) {
        Device base = deviceRepository.get(id);

        if (base == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }

        if (!(base instanceof AirConditionerDevice conditionerDevice)) {
            throw new WrongDeviceTypeException("Device is not an air conditioner");
        }

        AirConditionerDevice updated = airConditionerService.autoAdjust(conditionerDevice);
        deviceRepository.save(updated);

        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Device> list() {
        return deviceRepository.getAll();
    }

    @GetMapping("/{id}")
    public Device get(@PathVariable UUID id) {
        Device dev = deviceRepository.get(id);
        if (dev == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return dev;
    }

    @PutMapping("/{id}")
    public Device update(@PathVariable UUID id, @Valid @RequestBody Device body) {
        Device existing = deviceRepository.get(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        Device updated = new Device(id, body.deviceType, body.name);
        deviceRepository.save(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Device removed = deviceRepository.delete(id);
        if (removed == null) {
            throw new ResourceNotFoundException("Device %s not found".formatted(id));
        }
        return ResponseEntity.noContent().build();
    }
}
