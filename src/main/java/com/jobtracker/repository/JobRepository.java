package com.jobtracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobtracker.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	Page<Job> findByUserId(Long userId, Pageable pageable);

	// For search + filter by user
	Page<Job> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndCompanyContainingIgnoreCase(
	    Long userId1, String title,
	    Long userId2, String company,
	    Pageable pageable
	);

	}