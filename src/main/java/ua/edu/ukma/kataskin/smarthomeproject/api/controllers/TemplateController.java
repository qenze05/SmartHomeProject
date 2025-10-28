package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.templates.TemplateApplier;

import java.util.Map;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {
    private final TemplateApplier applier;

    public TemplateController(TemplateApplier applier) {
        this.applier = applier;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String name) {
        if (!applier.exists(name)) {
            throw new ResourceNotFoundException("Template %s not found".formatted(name));
        }
        return ResponseEntity.ok(applier.resolve(name));
    }
}

