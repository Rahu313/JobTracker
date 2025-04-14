package com.jobtracker.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobtracker.dto.ApiResponse;
import com.jobtracker.model.Job;
import com.jobtracker.service.JobService;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*") // for Angular access
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse> postJob(@RequestBody Job job) {
        Job savedJob = jobService.saveJob(job);
        if(savedJob!=null) {
        	 return ResponseEntity.ok(new ApiResponse(true, "Job posted successfully.", Collections.emptyList()));	
        }
        return ResponseEntity.ok(new ApiResponse(false, "An Error Occured.", Collections.emptyList()));
    }
}
