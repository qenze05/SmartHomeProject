package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.kataskin.smarthomeproject.api.auth.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtUtil.generateToken(username, "ADMIN");
            return Map.of("token", token);
        } else if ("user".equals(username) && "user123".equals(password)) {
            String token = jwtUtil.generateToken(username, "USER");
            return Map.of("token", token);
        }
        return Map.of("error", "Invalid credentials");
    }
}
