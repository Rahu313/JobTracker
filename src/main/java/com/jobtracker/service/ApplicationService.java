package com.jobtracker.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
