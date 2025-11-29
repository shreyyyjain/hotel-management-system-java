package com.shrey.hotel.controller;

import com.shrey.hotel.model.User;
import com.shrey.hotel.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            String fullName = body.get("fullName");
            User user = authService.register(email, password, fullName);
            Map<String, Object> resp = new HashMap<>();
            resp.put("token", "dummy-token");
            resp.put("user", user);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return authService.authenticate(email, password)
                .map(user -> ResponseEntity.ok(Map.of("token", "dummy-token", "user", user)))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message", "Invalid credentials")));
    }
}
