package com.jobtracker.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobtracker.model.Job;
import com.jobtracker.repository.JobRepository;

import jakarta.persistence.criteria.Predicate;

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

//    public Page<Job> getJobs(Long userId, int page, int size, String sort, String direction, String search) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
//        
//        if (search != null && !search.trim().isEmpty()) {
//            return jobRepository.findByUserIdAndTitleContainingIgnoreCase(userId, search, pageable);
//        }
//
//        return jobRepository.findByUserId(userId, pageable);
//    }
    public Page<Job> getJobs(Long userId, int page, int size, String sort, String direction, String search, String status) {
        Sort sortOrder = direction.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        System.out.println(sortOrder);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Specification<Job> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Only show jobs for this user
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // Search by title, company, location
            if (search != null && !search.isEmpty()) {
                String likeSearch = "%" + search.toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), likeSearch);
                Predicate companyMatch = cb.like(cb.lower(root.get("company")), likeSearch);
                Predicate locationMatch = cb.like(cb.lower(root.get("location")), likeSearch);
                predicates.add(cb.or(titleMatch, companyMatch, locationMatch));
            }

            // Filter by status
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return jobRepository.findAll(spec, pageable);
    }public Page<Job> getAllActiveJobs(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        if (search != null && !search.isEmpty()) {
            return jobRepository.findByStatusAndTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(
                "active", search, search, pageable
            );
        } else {
            return jobRepository.findByStatus("active", pageable);
        }
    }

    
}
