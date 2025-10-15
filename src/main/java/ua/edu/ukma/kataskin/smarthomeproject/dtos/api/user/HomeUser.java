package ua.edu.ukma.kataskin.smarthomeproject.dtos.api.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record HomeUser(
        UUID id,
        @NotNull(message = "User role must not be null")
        UserRole role,
        @Size(min = 2, max = 32, message = "Device name must be 2 to 32 characters long")
        String name
) {
}
