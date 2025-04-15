package com.jobtracker.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobtracker.model.User;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.util.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailService emailService;

	public boolean saveUser(String name, String email, String rawPassword, String role, String skills,
			String companyName, String companyWebsite, String resumeFileName) {
		try {
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(rawPassword));
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

		String token = jwtUtil.generateToken(user.getEmail());
		return Map.of("token", token, "user", user);
	}

	public User updateProfile(String email, User updatedUser) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		user.setName(updatedUser.getName());
		user.setRole(updatedUser.getRole());
		user.setProfilePic(updatedUser.getProfilePic());

		return userRepository.save(user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean handleForgotPassword(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (!optionalUser.isPresent())
			return false;

		User user = optionalUser.get();

		String tempPassword = UUID.randomUUID().toString().substring(0, 8); // Generate temp password

		// Hash password (BCrypt)
		String hashedPassword = new BCryptPasswordEncoder().encode(tempPassword);
		user.setPassword(hashedPassword);
		userRepository.save(user);

		try {
			emailService.sendForgotPasswordTemplate(user.getEmail(), user.getName(), tempPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	public boolean updateUserProfile(Long userId, String name, String rawPassword, String role, String skills,
            String companyName, String companyWebsite, MultipartFile resumeFile) {
try {
Optional<User> optionalUser = userRepository.findById(userId);
if (!optionalUser.isPresent()) return false;

User user = optionalUser.get();
user.setName(name);

if (rawPassword != null && !rawPassword.trim().isEmpty()) {
user.setPassword(passwordEncoder.encode(rawPassword));
}

if ("applicant".equalsIgnoreCase(role)) {
user.setSkills(skills);

if (resumeFile != null && !resumeFile.isEmpty()) {
String uploadsDir = "uploads/resumes/";
String realPath = new File(uploadsDir).getAbsolutePath();

File uploadDir = new File(realPath);
if (!uploadDir.exists()) uploadDir.mkdirs();

String fileName = System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();
Path filePath = Paths.get(realPath, fileName);
Files.copy(resumeFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

// Save full file URL
user.setResumeFile(fileName);
}
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
	public Long findUserIdByEmail(String email) {
	    Optional<User> user = userRepository.findByEmail(email);  // assuming you have a method in your repository
	    
	    // Check if the user is present in the Optional
	    if (user.isPresent()) {
	        return user.get().getId();  // Assuming you have a 'getId()' method in User entity
	    }
	    
	    // Return null if user is not found, or you could throw an exception if preferred
	    return null;
	}
}
