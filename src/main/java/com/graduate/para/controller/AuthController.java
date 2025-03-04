package com.graduate.para.controller;

import com.graduate.para.controller.dto.LoginRequest;
import com.graduate.para.controller.dto.RegisterRequest;
import com.graduate.para.model.User;
import com.graduate.para.service.UserService;
import com.graduate.para.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for username: {}", request.getUsername());
        try {
            // 创建用户对象
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());

            // 注册用户
            if (userService.register(user)) {
                log.info("Successfully registered user: {}", request.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Registration successful");
                return ResponseEntity.ok(response);
            } else {
                log.warn("Registration failed for username: {} - User already exists", request.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("error", "Username or email already exists");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Error during registration for username: {}", request.getUsername(), e);
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Received login request for username: {}", request.getUsername());
        try {
            // 验证用户
            User user = userService.login(request.getUsername(), request.getPassword());
            
            if (user != null) {
                // 生成JWT令牌
                String token = jwtUtils.generateToken(user);
                
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", user);
                
                log.info("Successfully logged in user: {}", request.getUsername());
                return ResponseEntity.ok(response);
            } else {
                log.warn("Login failed for username: {} - Invalid credentials", request.getUsername());
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid username or password");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Error during login for username: {}", request.getUsername(), e);
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        log.info("Checking username availability: {}", username);
        boolean exists = userService.checkUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        log.info("Checking email availability: {}", email);
        boolean exists = userService.checkEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
} 