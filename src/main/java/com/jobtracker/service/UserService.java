package com.jobtracker.service;



import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobtracker.model.User;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.util.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean saveUser(String name, String email, String password, String role,
                            String skills, String companyName, String companyWebsite,
                            String resumeFileName) {

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password); // You should hash this in real apps
            user.setRole(role);

            if ("applicant".equalsIgnoreCase(role)) {
                user.setSkills(skills);
                user.setResumeFile(resumeFileName);
            } else if ("poster".equalsIgnoreCase(role)) {
                user.setCompanyName(companyName);
                user.setCompanyWebsite(companyWebsite);
            }

            userRepository.save(user);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> login(User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user);
        return Map.of("token", token, "user", user);
    }

    public User updateProfile(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setRole(updatedUser.getRole());
        user.setProfilePic(updatedUser.getProfilePic());

        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

