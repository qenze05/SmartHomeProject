package ua.edu.ukma.kataskin.smarthomeproject.api.dto;
import java.util.Set; import java.util.UUID;
public record DeviceDto(UUID id, String name, String type, Long roomId, Set<String> tags) {}
