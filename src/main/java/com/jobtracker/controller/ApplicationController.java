package com.jobtracker.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobtracker.dto.ApiResponse;
import com.jobtracker.dto.ApplicationDTO;
import com.jobtracker.model.Application;
import com.jobtracker.service.ApplicationService;
import com.jobtracker.service.EncryptionService;
import com.jobtracker.service.JobService;
import com.jobtracker.service.UserService;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private UserService userService;
    
    @Autowired
    private JobService jobService;
   
    @PostMapping("/apply")
    public ResponseEntity<Application> submitApplication(
            @RequestParam String jobId,
            @RequestParam MultipartFile resumeFile,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String linkedin,
            @RequestParam Double currentCtc,
            @RequestParam Double expectedCtc) {

        // Decrypt jobId
        Long jobIdDecrypt = encryptionService.decrypt(jobId);

        // Extract userId from token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserIdByEmail(username);

        // Handle file upload (if the file is new or different from the old one)
        String fileName = handleFileUpload(resumeFile);

        // Create Application object and save
        Application application = new Application();
        application.setLinkedinUrl(linkedin);
        application.setCurrentCtc(currentCtc);
        application.setExpectedCtc(expectedCtc);
        application.setResumeFile(fileName);

        Application submittedApplication = applicationService.submitApplication(jobIdDecrypt, userId, application);
        return ResponseEntity.ok(submittedApplication);
    }

    private String handleFileUpload(MultipartFile resumeFile) {
        if (resumeFile != null && !resumeFile.isEmpty()) {
            // Check if the resume already exists in the system (by filename or hash comparison)
            String existingFilePath = "uploads/resumes/" + resumeFile.getOriginalFilename();
            File existingFile = new File(existingFilePath);
            
            if (existingFile.exists()) {
                // If the file already exists, do not upload again. Simply return the existing file name.
                return resumeFile.getOriginalFilename();
            } else {
                try {
                    // If the file doesn't exist, proceed with the upload
                    String uploadsDir = "uploads/resumes/";
                    File uploadDir = new File(uploadsDir);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String fileName = System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();
                    Path filePath = Path.of(uploadsDir, fileName);
                    Files.copy(resumeFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    return fileName;
                } catch (Exception e) {
                    throw new RuntimeException("File upload failed", e);
                }
            }
        }
        return null;
    }
    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        try {
            // Get logged-in user id
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Long userId = userService.findUserIdByEmail(username);

            // Fetch paginated applications
            Page<Application> applications = applicationService.getUserApplications(userId, page, size, search);

            // Convert Application entity to ApplicationDTO
            List<ApplicationDTO> applicationList = applications.getContent().stream()
                    .map(app -> {
                        String jobTitle = jobService.getJobById(app.getJob().getId()).getTitle();
                        String companyName = jobService.getJobById(app.getJob().getId()).getCompany();
                        String salaryRange = jobService.getJobById(app.getJob().getId()).getSalaryRange();
                        return new ApplicationDTO(
                                app.getId(),
                                jobTitle,
                                companyName,
                                app.getLinkedinUrl(),
                                app.getCurrentCtc(),
                                app.getExpectedCtc(),
                                app.getStatus().name(), // Assuming status is ENUM
                                app.getResumeFile(),
                                app.getAppliedAt(),
                                salaryRange
                        );
                    })
                    .toList();

            // Build Response
            Map<String, Object> response = new HashMap<>();
            response.put("applications", applicationList);
            response.put("totalPages", applications.getTotalPages());
            response.put("totalElements", applications.getTotalElements());
            response.put("currentPage", applications.getNumber());

            return ResponseEntity.ok(new ApiResponse(true, "Applications fetched successfully", response));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, "Error: " + e.getMessage(), Collections.emptyMap()));
        }
    }

}
