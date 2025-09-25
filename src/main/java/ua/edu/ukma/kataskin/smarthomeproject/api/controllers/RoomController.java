package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final Map<Long, RoomDto> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public record RoomDto(
            @NotBlank String name,
            String description,
            Set<String> deviceIds
    ) {}

    public record RoomPatchDto(
            String name,
            String description,
            Set<String> deviceIds
    ) {}

    public record RoomView(
            Long id, String name, String description, Set<String> deviceIds
    ) {}


    @GetMapping
    public List<RoomView> list() {
        return store.entrySet().stream()
                .map(e -> toView(e.getKey(), e.getValue()))
                .toList();
    }

    @GetMapping("/{id}")
    public RoomView get(@PathVariable Long id) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        return toView(id, dto);
    }

    @PostMapping
    public ResponseEntity<RoomView> create(@Valid @RequestBody RoomDto dto) {
        long id = seq.getAndIncrement();
        var normalized = new RoomDto(
                dto.name(),
                dto.description(),
                dto.deviceIds() != null ? new LinkedHashSet<>(dto.deviceIds()) : new LinkedHashSet<>()
        );
        store.put(id, normalized);
        var body = toView(id, normalized);
        return ResponseEntity.created(URI.create("/api/v1/rooms/" + id)).body(body);
    }

    @PutMapping("/{id}")
    public RoomView put(@PathVariable Long id, @Valid @RequestBody RoomDto dto) {
        if (!store.containsKey(id)) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        var normalized = new RoomDto(
                dto.name(),
                dto.description(),
                dto.deviceIds() != null ? new LinkedHashSet<>(dto.deviceIds()) : new LinkedHashSet<>()
        );
        store.put(id, normalized);
        return toView(id, normalized);
    }

    @PatchMapping("/{id}")
    public RoomView patch(@PathVariable Long id, @RequestBody RoomPatchDto patch) {
        var current = store.get(id);
        if (current == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));

        var newName = patch.name() != null ? patch.name() : current.name();
        var newDesc = patch.description() != null ? patch.description() : current.description();
        Set<String> newDevices = patch.deviceIds() != null
                ? new LinkedHashSet<>(patch.deviceIds())
                : (current.deviceIds() != null ? new LinkedHashSet<>(current.deviceIds()) : new LinkedHashSet<>());

        var updated = new RoomDto(newName, newDesc, newDevices);
        store.put(id, updated);
        return toView(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (store.remove(id) == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
    }

    // зв’язування пристроїв з кімнатою
    @GetMapping("/{id}/devices")
    public Set<String> devices(@PathVariable Long id) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        return dto.deviceIds() != null ? dto.deviceIds() : Set.of();
    }

    @PostMapping("/{id}/devices/{deviceId}")
    public RoomView addDevice(@PathVariable Long id, @PathVariable String deviceId) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        LinkedHashSet<String> set = dto.deviceIds() != null ? new LinkedHashSet<>(dto.deviceIds()) : new LinkedHashSet<>();
        set.add(deviceId);
        var updated = new RoomDto(dto.name(), dto.description(), set);
        store.put(id, updated);
        return toView(id, updated);
    }

    @DeleteMapping("/{id}/devices/{deviceId}")
    public RoomView removeDevice(@PathVariable Long id, @PathVariable String deviceId) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        LinkedHashSet<String> set = dto.deviceIds() != null ? new LinkedHashSet<>(dto.deviceIds()) : new LinkedHashSet<>();
        set.remove(deviceId);
        var updated = new RoomDto(dto.name(), dto.description(),  set);
        store.put(id, updated);
        return toView(id, updated);
    }

    private static RoomView toView(Long id, RoomDto dto) {
        return new RoomView(id, dto.name(), dto.description(),
                dto.deviceIds() != null ? dto.deviceIds() : Set.of());
    }
}
