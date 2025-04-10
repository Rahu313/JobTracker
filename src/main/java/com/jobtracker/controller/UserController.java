package com.jobtracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobtracker.model.User;
import com.jobtracker.service.UserService;
import com.jobtracker.util.JwtUtil;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        return ResponseEntity.ok(userService.signup(user));
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
