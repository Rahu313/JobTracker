package com.jobtracker.repository;

import com.jobtracker.model.Application;
import com.jobtracker.model.User;
import com.jobtracker.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	public  Page<Application> findByApplicantAndJob_TitleContainingIgnoreCaseOrApplicantAndJob_CompanyContainingIgnoreCase(
		    User applicant, String jobTitle, User applicant2, String companyName, Pageable pageable);


    // OR simple version if no search
    Page<Application> findByApplicant(User applicant, Pageable pageable);
}
