package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Validated
@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final Map<Long, ScheduleDto> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    // куди застосовується розклад: кімната чи пристрій
    public enum TargetType { ROOM, DEVICE }

    // яка дія виконується розкладом
    public enum Action { ON, OFF, TOGGLE }

    public record ScheduleDto(
            @NotNull TargetType targetType,
            @NotBlank String targetId,            // id кімнати або пристрою
            @NotNull @Size(min = 1) Set<DayOfWeek> daysOfWeek,
            @NotNull LocalTime startTime,
            @NotNull LocalTime endTime,
            @NotNull Action action,
            @NotBlank String timezone             // "Europe/Kyiv"
    ) {}

    public record SchedulePatchDto(
            TargetType targetType,
            String targetId,
            Set<DayOfWeek> daysOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Action action,
            String timezone
    ) {}

    public record ScheduleView(
            Long id,
            TargetType targetType,
            String targetId,
            Set<DayOfWeek> daysOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Action action,
            String timezone
    ) {}


    @GetMapping
    public List<ScheduleView> list() {
        return store.entrySet().stream().map(e -> toView(e.getKey(), e.getValue())).toList();
    }

    @GetMapping("/{id}")
    public ScheduleView get(@PathVariable Long id) {
        var dto = store.get(id);
        if (dto == null) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));
        return toView(id, dto);
    }

    @PostMapping
    public ResponseEntity<ScheduleView> create(@Valid @RequestBody ScheduleDto dto) {
        validateTimes(dto.startTime(), dto.endTime()); // проста валідація
        long id = seq.getAndIncrement();
        var normalized = normalize(dto);
        store.put(id, normalized);
        var body = toView(id, normalized);
        return ResponseEntity.created(URI.create("/api/v1/schedules/" + id)).body(body);
    }

    @PutMapping("/{id}")
    public ScheduleView put(@PathVariable Long id, @Valid @RequestBody ScheduleDto dto) {
        if (!store.containsKey(id)) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));
        validateTimes(dto.startTime(), dto.endTime());
        var normalized = normalize(dto);
        store.put(id, normalized);
        return toView(id, normalized);
    }

    @PatchMapping("/{id}")
    public ScheduleView patch(@PathVariable Long id, @RequestBody SchedulePatchDto patch) {
        var current = store.get(id);
        if (current == null) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));

        var dto = new ScheduleDto(
                patch.targetType() != null ? patch.targetType() : current.targetType(),
                patch.targetId() != null ? patch.targetId() : current.targetId(),
                patch.daysOfWeek() != null ? new LinkedHashSet<>(patch.daysOfWeek()) : current.daysOfWeek(),
                patch.startTime() != null ? patch.startTime() : current.startTime(),
                patch.endTime() != null ? patch.endTime() : current.endTime(),
                patch.action() != null ? patch.action() : current.action(),
                patch.timezone() != null ? patch.timezone() : current.timezone()
        );
        validateTimes(dto.startTime(), dto.endTime());
        var normalized = normalize(dto);
        store.put(id, normalized);
        return toView(id, normalized);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (store.remove(id) == null) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));
    }

//filters

    @GetMapping("/by-room/{roomId}")
    public List<ScheduleView> byRoom(@PathVariable String roomId) {
        return store.entrySet().stream()
                .filter(e -> e.getValue().targetType() == TargetType.ROOM && e.getValue().targetId().equals(roomId))
                .map(e -> toView(e.getKey(), e.getValue()))
                .toList();
    }

    @GetMapping("/by-device/{deviceId}")
    public List<ScheduleView> byDevice(@PathVariable String deviceId) {
        return store.entrySet().stream()
                .filter(e -> e.getValue().targetType() == TargetType.DEVICE && e.getValue().targetId().equals(deviceId))
                .map(e -> toView(e.getKey(), e.getValue()))
                .toList();
    }


    private static void validateTimes(LocalTime start, LocalTime end) {

        if (start.equals(end)) throw new IllegalArgumentException("startTime must differ from endTime");
    }

    private static ScheduleDto normalize(ScheduleDto in) {
        return new ScheduleDto(
                in.targetType(),
                in.targetId(),
                new LinkedHashSet<>(in.daysOfWeek()),
                in.startTime(),
                in.endTime(),
                in.action(),
                in.timezone()
        );
    }

    private static ScheduleView toView(Long id, ScheduleDto dto) {
        return new ScheduleView(id, dto.targetType(), dto.targetId(), dto.daysOfWeek(),
                dto.startTime(), dto.endTime(), dto.action(), dto.timezone());
    }
}
