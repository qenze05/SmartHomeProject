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
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final Map<Long, ScheduleDto> store = new HashMap<>();

    public enum TargetType {ROOM, DEVICE}

    public enum Action {ON, OFF, TOGGLE}

    public record ScheduleDto(
            @NotNull TargetType targetType,
            @NotBlank String targetId,
            @NotNull @Size(min = 1) Set<DayOfWeek> daysOfWeek,
            @NotNull LocalTime startTime,
            @NotNull LocalTime endTime,
            @NotNull Action action,
            @NotBlank String timezone
    ) {
    }

    public record ScheduleView(
            Long id,
            TargetType targetType,
            String targetId,
            Set<DayOfWeek> daysOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Action action,
            String timezone
    ) {
    }


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

    @PutMapping("/{id}")
    public ScheduleView put(@PathVariable Long id, @Valid @RequestBody ScheduleDto dto) {
        if (!store.containsKey(id)) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));
        validateTimes(dto.startTime(), dto.endTime());
        var normalized = normalize(dto);
        store.put(id, normalized);
        return toView(id, normalized);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (store.remove(id) == null) throw new ResourceNotFoundException("Schedule %d not found".formatted(id));
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
