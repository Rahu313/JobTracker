package com.jobtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobtracker.model.Application;
import com.jobtracker.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/submit")
    public ResponseEntity<Application> submitApplication(
            @RequestParam Long jobId,
            @RequestParam Long userId,
            @RequestBody Application application) {
        Application submittedApplication = applicationService.submitApplication(jobId, userId, application);
        return ResponseEntity.ok(submittedApplication);
    }
}