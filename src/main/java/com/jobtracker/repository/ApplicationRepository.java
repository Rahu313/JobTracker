package com.jobtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobtracker.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
