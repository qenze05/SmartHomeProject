package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ForbiddenOperationException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.user.HomeUser;
import ua.edu.ukma.kataskin.smarthomeproject.models.api.user.UserRole;

import java.net.URI;
import java.util.*;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Map<UUID, HomeUser> store = new HashMap<>();

    @PostMapping
    public ResponseEntity<HomeUser> create(@Valid @RequestBody HomeUser body) {
        UUID id = UUID.randomUUID();
        HomeUser created = new HomeUser(id, body.role(), body.name());
        store.put(id, created);
        return ResponseEntity.created(URI.create("/api/users/" + id)).body(created);
    }

    @GetMapping
    public List<HomeUser> list() {
        return new ArrayList<>(store.values());
    }

    @GetMapping("/{id}")
    public HomeUser get(@PathVariable UUID id) {
        HomeUser user = store.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("User %s not found".formatted(id));
        }
        return user;
    }

    @PutMapping("/{id}")
    public HomeUser update(@PathVariable UUID id, @Valid @RequestBody HomeUser body) {
        HomeUser existing = store.get(id);
        if (existing == null) {
            throw new ResourceNotFoundException("User %s not found".formatted(id));
        }
        HomeUser updated = new HomeUser(id, body.role(), body.name());
        store.put(id, updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        HomeUser user = store.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("User %s not found".formatted(id));
        }
        if (user.role() == UserRole.OWNER) {
            throw new ForbiddenOperationException("Owner account can't be deleted");
        }
        store.remove(id);
        return ResponseEntity.noContent().build();
    }
}
