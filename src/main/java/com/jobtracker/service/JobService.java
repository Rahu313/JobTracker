package com.jobtracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobtracker.model.Job;
import com.jobtracker.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // Save a new job
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    // Update an existing job
    public Job updateJob(Job job) {
        Optional<Job> existingJob = jobRepository.findById(job.getId());
        if (existingJob.isPresent()) {
            Job updatedJob = existingJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setCompany(job.getCompany());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setType(job.getType());
            updatedJob.setDescription(job.getDescription());
            return jobRepository.save(updatedJob);
        }
        return null;
    }

    // Delete a job by ID
    public boolean deleteJob(Long jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            jobRepository.deleteById(jobId);
            return true;
        }
        return false;
    }

    // Get job by ID
    public Job getJobById(Long jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        return job.orElse(null);
    }

    public List<Job> getJobs(Long userId, int page, int size, String sort, String direction, String search) {
        Sort.Order order = "desc".equalsIgnoreCase(direction)
            ? Sort.Order.desc(sort)
            : Sort.Order.asc(sort);

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Job> jobPage;

        if (search != null && !search.isEmpty()) {
            // Search with user filter
            jobPage = jobRepository.findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndCompanyContainingIgnoreCase(
                userId, search, userId, search, pageable);
        } else {
            // No search, just filter by user
            jobPage = jobRepository.findByUserId(userId, pageable);
        }

        return jobPage.getContent();
    }

}
