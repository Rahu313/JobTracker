package com.jobtracker.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobtracker.dto.ApiResponse;
import com.jobtracker.dto.LoginRequest;
import com.jobtracker.model.User;
import com.jobtracker.service.UserService;
import com.jobtracker.util.JwtUtil;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> signup(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "skills", required = false) String skills,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyWebsite", required = false) String companyWebsite,
            @RequestPart(value = "resume", required = false) MultipartFile resume
    ) {
        try {
            String resumeFileName = null;

            if ("applicant".equalsIgnoreCase(role) && resume != null) {
                Path uploadPath = Paths.get("uploads/resumes");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                resumeFileName = UUID.randomUUID() + "_" + resume.getOriginalFilename();
                Path filePath = uploadPath.resolve(resumeFileName);
                Files.write(filePath, resume.getBytes());
            }

            boolean saved = userService.saveUser(name, email, password, role, skills, companyName, companyWebsite, resumeFileName);

            if (saved) {
                return ResponseEntity.ok(new ApiResponse(true, "User registered successfully.", Collections.emptyList()));
            } else {
                return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to register user.", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "An error occurred: " + e.getMessage(), Collections.emptyList()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> data = userService.login(new User(loginRequest.getEmail(), loginRequest.getPassword()));
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", data));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestParam Long userId,
                                           @RequestParam String name,
                                           @RequestParam(required = false) String newPassword,
                                           @RequestParam String role,
                                           @RequestParam(required = false) String skills,
                                           @RequestParam(required = false) String companyName,
                                           @RequestParam(required = false) String companyWebsite,
                                           @RequestParam(required = false) MultipartFile resume) {
        boolean updated = userService.updateUserProfile(userId, name, newPassword, role, skills, companyName, companyWebsite, resume);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", true, "message", "Profile updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", "Error updating profile"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(@RequestHeader("Authorization") String token) {
       System.out.println("inside method");
    	String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
        System.out.println(email);
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(new ApiResponse(true, "User profile fetched", user)))
                .orElseGet(() -> ResponseEntity.ok(new ApiResponse(false, "User not found", null)));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        boolean result = userService.handleForgotPassword(email);
        if (result) {
            return ResponseEntity.ok(new ApiResponse(true, "Temporary password sent to your email", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "Email not found", null));
        }
    }
    @GetMapping("/uploads/resumes/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("G:/JobTrackerApp/JobTracker-Backend/uploads/resumes/").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
