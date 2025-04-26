package com.jobtracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobtracker.enums.JobStatus;
import com.jobtracker.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	Page<Job> findByStatus(JobStatus status, Pageable pageable);

	Page<Job> findByStatusAndTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(
	    JobStatus status, String titleKeyword, String companyKeyword, Pageable pageable
	);

	Page<Job> findAll(Specification<Job> spec, Pageable pageable);

	}