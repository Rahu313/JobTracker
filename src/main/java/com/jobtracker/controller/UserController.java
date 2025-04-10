package com.jobtracker.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobtracker.model.User;
import com.jobtracker.service.UserService;
import com.jobtracker.util.JwtUtil;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> signup(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyWebsite", required = false) String companyWebsite,
            @RequestPart(value = "resume", required = false) MultipartFile resume
    ) {
        Map<String, Object> response = new HashMap<>();
        String resumeFileName = null;

        try {
            // Save resume if role is applicant and file is provided
            if ("applicant".equalsIgnoreCase(role) && resume != null) {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                resumeFileName = UUID.randomUUID() + "_" + resume.getOriginalFilename();
                Path filePath = uploadPath.resolve(resumeFileName);
                Files.write(filePath, resume.getBytes());
            }

            // Encrypt password
            String encodedPassword = new BCryptPasswordEncoder().encode(password);

            // Save user
            boolean saved = userService.saveUser(name, email, encodedPassword, role, skills, companyName, companyWebsite, resumeFileName);

            if (saved) {
                response.put("status", true);
                response.put("message", "User registered successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("message", "Failed to register user.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("status", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


       @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
                                           @RequestBody User updatedUser) {
        String email = JwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(userService.updateProfile(email, updatedUser));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String email = JwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
