package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Validated
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final Map<Long, RoomDto> store = new HashMap<>();

    public record RoomDto(
            @NotBlank(message = "name is required")
            @Size(max = 50, message = "name must be <= 50 chars")
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (store.remove(id) == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
    }

    @GetMapping("/{id}/devices")
    public Set<String> devices(@PathVariable Long id) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Room %d not found".formatted(id));
        return dto.deviceIds() != null ? dto.deviceIds() : Set.of();
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
