package ua.edu.ukma.kataskin.smarthomeproject.db.mappers;

import org.mapstruct.*;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.groups.GroupEntity;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.groups.GroupRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.repository.room.RoomRepository;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceDTO;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mappings({
            @Mapping(source = "deviceType", target = "deviceType"),
            @Mapping(source = "room.id", target = "roomId", qualifiedByName = "longToString"),
            @Mapping(source = "groups", target = "groupIds", qualifiedByName = "groupsToStringIds")
    })
    DeviceDTO toDto(DeviceEntity entity);

    List<DeviceDTO> toDtoList(List<DeviceEntity> entities);

    @Mappings({
            @Mapping(target = "deviceType", source = "deviceType", defaultValue = "UNSPECIFIED"),
            @Mapping(target = "room", ignore = true),
            @Mapping(target = "groups", ignore = true)
    })
    DeviceEntity toEntity(DeviceDTO dto,
                          @Context RoomRepository roomRepo,
                          @Context GroupRepository groupRepo);

    @AfterMapping
    default void linkRoom(DeviceDTO dto,
                          @MappingTarget DeviceEntity entity,
                          @Context RoomRepository roomRepo) {
        if (dto.roomId != null && !dto.roomId.isBlank()) {
            roomRepo.findById(Long.valueOf(dto.roomId)).ifPresent(entity::setRoom);
        } else {
            entity.setRoom(null);
        }
    }

    @AfterMapping
    default void linkGroups(DeviceDTO dto,
                            @MappingTarget DeviceEntity entity,
                            @Context GroupRepository groupRepo) {
        if (dto.groupIds == null) {
            entity.setGroups(new HashSet<>());
            return;
        }
        var ids = dto.groupIds.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(UUID::fromString)
                .toList();

        var found = new HashSet<>(groupRepo.findAllById(ids));
        entity.setGroups(found);
    }

    @Named("longToString")
    default String longToString(Object id) {
        return id == null ? null : id.toString();
    }

    @Named("groupsToStringIds")
    default List<String> groupsToStringIds(Set<GroupEntity> groups) {
        if (groups == null || groups.isEmpty()) return List.of();
        return groups.stream()
                .map(GroupEntity::getId)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
