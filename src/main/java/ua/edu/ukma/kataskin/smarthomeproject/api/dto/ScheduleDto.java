package ua.edu.ukma.kataskin.smarthomeproject.api.dto;
import java.time.*; import java.util.*;
public record ScheduleDto(
        Long id, String targetType, Long roomId, java.util.UUID deviceId,
        Set<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime,
        String action, String timezone
) {}
