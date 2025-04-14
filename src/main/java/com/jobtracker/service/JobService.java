package com.jobtracker.service;

import com.jobtracker.model.Job;
import com.jobtracker.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // Get a list of jobs with pagination, sorting, and searching
    public List<Job> getJobs(int page, int size, String sort, String direction, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        if ("desc".equalsIgnoreCase(direction)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
        }

        Page<Job> jobPage;
        if (search != null && !search.isEmpty()) {
            jobPage = jobRepository.findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(search, search, pageable);
        } else {
            jobPage = jobRepository.findAll(pageable);
        }
        
        return jobPage.getContent();
    }
}
