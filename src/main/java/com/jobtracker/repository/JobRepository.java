package com.jobtracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobtracker.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	Page<Job> findByStatus(String status, Pageable pageable);

	Page<Job> findByStatusAndTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(
	    String status, String titleKeyword, String companyKeyword, Pageable pageable
	);

	Page<Job> findAll(Specification<Job> spec, Pageable pageable);

	}