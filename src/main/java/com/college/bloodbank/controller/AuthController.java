package com.college.bloodbank.controller;

import com.college.bloodbank.entity.AppUser;
import com.college.bloodbank.entity.Role;
import com.college.bloodbank.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    public record LoginRequest(@NotBlank String username, @NotBlank String password, @NotBlank String role) {}

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Role role;
        try { role = Role.valueOf(request.role().toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("Role must be ADMIN or USER."); }
        AppUser user = authService.login(request.username(), request.password(), role);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole().name());
        return Map.of("message", "Login successful", "username", user.getUsername(), "role", user.getRole().name());
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Logged out successfully");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        Object username = session.getAttribute("username");
        Object role = session.getAttribute("role");
        if (username == null) return Map.of("loggedIn", false);
        return Map.of("loggedIn", true, "username", username, "role", role);
    }
}
