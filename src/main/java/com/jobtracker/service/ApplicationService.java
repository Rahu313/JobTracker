package com.jobtracker.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.jobtracker.model.Application;
import com.jobtracker.model.Job;
import com.jobtracker.model.User;
import com.jobtracker.repository.ApplicationRepository;
import com.jobtracker.repository.JobRepository;
import com.jobtracker.repository.UserRepository;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public Application submitApplication(Long jobId, Long userId, Application application) {
        Optional<Job> job = jobRepository.findById(jobId);
        Optional<User> user = userRepository.findById(userId);

        if (job.isPresent() && user.isPresent()) {
            application.setJob(job.get());
            application.setApplicant(user.get());
            return applicationRepository.save(application);
        } else {
            throw new IllegalArgumentException("Invalid job or applicant");
        }
    }
    public Page<Application> getUserApplications(Long userId, int page, int size, String search) {
        // Find the User object by userId
        User applicant = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());

        if (search != null && !search.trim().isEmpty()) {
            // If search is provided, search by job title or company name
            return applicationRepository.findByApplicantAndJob_TitleContainingIgnoreCaseOrApplicantAndJob_CompanyContainingIgnoreCase(
                    applicant, search, applicant, search, pageable);
        } else {
            // If no search term, just find by applicant
            return applicationRepository.findByApplicant(applicant, pageable);
        }
    }


}
