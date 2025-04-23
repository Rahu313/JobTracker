package com.jobtracker.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobtracker.dto.ApiResponse;
import com.jobtracker.model.Job;
import com.jobtracker.model.User;
import com.jobtracker.service.JobService;
import com.jobtracker.service.UserService;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*") // for Angular access
public class JobController {

	@Autowired
	private JobService jobService;
	@Autowired
	private UserService userService;

	@PostMapping("/post-job")
	public ResponseEntity<ApiResponse> postJob(@RequestBody Job job) {
		try {
			// Get currently authenticated user's email
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();

			// Fetch user from the DB using email
			Long userId = userService.findUserIdByEmail(username);
			if (userId == null) {
				return ResponseEntity.ok(new ApiResponse(false, "User not found.", Collections.emptyList()));
			}

			// Fetch full user entity from DB
			User user = userService.findById(userId); // <-- Make sure you have this method in your UserService

			// Set the user into the job object
			job.setUser(user);

			// Save the job
			Job savedJob = jobService.saveJob(job);

			if (savedJob != null) {
				return ResponseEntity.ok(
						new ApiResponse(true, "Job posted successfully.", Collections.singletonList(savedJob.getId())));
			}
			return ResponseEntity
					.ok(new ApiResponse(false, "An error occurred while posting the job.", Collections.emptyList()));

		} catch (Exception e) {
			return ResponseEntity
					.ok(new ApiResponse(false, "Exception occurred: " + e.getMessage(), Collections.emptyList()));
		}
	}

	// Edit an existing job (Update Job) - Uses Request Body with ID
	@PostMapping("/edit")
	public ResponseEntity<ApiResponse> editJob(@RequestBody Job job) {
		if (job.getId() == null) {
			return ResponseEntity
					.ok(new ApiResponse(false, "Job ID is required for updating.", Collections.emptyList()));
		}
		Job updatedJob = jobService.updateJob(job);
		if (updatedJob != null) {
			return ResponseEntity.ok(
					new ApiResponse(true, "Job updated successfully.", Collections.singletonList(updatedJob.getId())));
		}
		return ResponseEntity.ok(new ApiResponse(false, "Job not found or update failed.", Collections.emptyList()));
	}

	// Delete a job by ID - Uses Request Body with ID
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> deleteJob(@RequestBody Job job) {
		if (job.getId() == null) {
			return ResponseEntity
					.ok(new ApiResponse(false, "Job ID is required for deletion.", Collections.emptyList()));
		}
		boolean deleted = jobService.deleteJob(job.getId());
		if (deleted) {
			return ResponseEntity.ok(new ApiResponse(true, "Job deleted successfully.", Collections.emptyList()));
		}
		return ResponseEntity.ok(new ApiResponse(false, "Job not found or delete failed.", Collections.emptyList()));
	}

	// Get job by ID - Securely get the job by id through Request Body
	@PostMapping("/getJobById")
	public ResponseEntity<ApiResponse> getJobById(@RequestBody Job job) {
		if (job.getId() == null) {
			return ResponseEntity
					.ok(new ApiResponse(false, "Job ID is required to fetch the job.", Collections.emptyList()));
		}
		Job fetchedJob = jobService.getJobById(job.getId());
		if (fetchedJob != null) {
			return ResponseEntity
					.ok(new ApiResponse(true, "Job fetched successfully.", Collections.singletonList(fetchedJob)));
		}
		return ResponseEntity.ok(new ApiResponse(false, "Job not found.", Collections.emptyList()));
	}
	@GetMapping("/job-list")
	public ResponseEntity<ApiResponse> getAllJobs(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sort,
	        @RequestParam(defaultValue = "asc") String direction,
	        @RequestParam(required = false) String search,
	        @RequestParam(required = false) String status // To filter active/inactive jobs
	) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        Long userId = userService.findUserIdByEmail(username);

	        // Pass new params to service layer
	        Page<Job> jobPage = jobService.getJobs(userId, page, size, sort, direction, search, status);

	        Map<String, Object> response = new HashMap<>();
	        response.put("jobs", jobPage.getContent());
	        response.put("totalPages", jobPage.getTotalPages());
	        response.put("totalElements", jobPage.getTotalElements());
	        response.put("currentPage", jobPage.getNumber());

	        return ResponseEntity.ok(new ApiResponse(true, "Jobs fetched successfully.", response));
	    } catch (Exception e) {
	        return ResponseEntity
	                .ok(new ApiResponse(false, "Error fetching jobs: " + e.getMessage(), Collections.emptyMap()));
	    }
	}
	@GetMapping("/browse-jobs")
	public ResponseEntity<ApiResponse> browseJobs(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size,
	    @RequestParam(required = false) String search
	) {
	    try {
	        Page<Job> jobs = jobService.getAllActiveJobs(page, size, search);

	        Map<String, Object> response = new HashMap<>();
	        response.put("jobs", jobs.getContent());
	        response.put("totalPages", jobs.getTotalPages());
	        response.put("totalElements", jobs.getTotalElements());
	        response.put("currentPage", jobs.getNumber());

	        return ResponseEntity.ok(new ApiResponse(true, "Active jobs fetched successfully", response));
	    } catch (Exception e) {
	        return ResponseEntity.ok(
	            new ApiResponse(false, "Error: " + e.getMessage(), Collections.emptyMap())
	        );
	    }
	}


	

}
