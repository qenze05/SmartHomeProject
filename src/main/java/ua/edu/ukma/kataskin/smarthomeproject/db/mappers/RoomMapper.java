package ua.edu.ukma.kataskin.smarthomeproject.db.mappers;

import org.mapstruct.*;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.RoomEntity;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.room.RoomDto;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "devicesCount", expression = "java(room.getDevices() == null ? 0 : room.getDevices().size())")
    RoomDto toDto(RoomEntity room);
}
