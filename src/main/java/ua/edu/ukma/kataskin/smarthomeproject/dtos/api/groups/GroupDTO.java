package ua.edu.ukma.kataskin.smarthomeproject.dtos.api.groups;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class GroupDTO {
    public static final String groupNameValidationMessage =
            "Group name must be 2 to 32 characters long";

    public UUID id;

    @Size(min = 2, max = 32, message = groupNameValidationMessage)
    public String name;

    @Size(max = 512, message = "Group description must be up to 512 characters long")
    public String description;

    public List<UUID> deviceIds;

    public GroupDTO() {
    }

    public GroupDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}