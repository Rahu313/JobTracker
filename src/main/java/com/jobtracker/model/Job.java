package com.jobtracker.model;

import java.time.LocalDateTime;

import com.jobtracker.enums.JobStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "jobs")
@Builder
public class Job {
    public Job(Long id, String title, String company, String location, String type, String description,
			String experienceRequired, String technologyStack, String salaryRange, LocalDateTime createdDate, User user,
			JobStatus status, String jobLevel) {
		super();
		this.id = id;
		this.title = title;
		this.company = company;
		this.location = location;
		this.type = type;
		this.description = description;
		this.experienceRequired = experienceRequired;
		this.technologyStack = technologyStack;
		this.salaryRange = salaryRange;
		this.createdDate = createdDate;
		this.user = user;
		this.status = status;
		this.jobLevel = jobLevel;
	}


	public Job() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private String location;
    private String type;

    @Column(length = 2000)
    private String description;

    private String experienceRequired;

    @Column(length = 1000)
    private String technologyStack;

    private String salaryRange;

    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

@Enumerated(EnumType.STRING)
@Column(name = "status", nullable = false)
private JobStatus status = JobStatus.ACTIVE; // default

    private String jobLevel; // Intern, Fresher, Junior, etc.


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getExperienceRequired() {
		return experienceRequired;
	}


	public void setExperienceRequired(String experienceRequired) {
		this.experienceRequired = experienceRequired;
	}


	public String getTechnologyStack() {
		return technologyStack;
	}


	public void setTechnologyStack(String technologyStack) {
		this.technologyStack = technologyStack;
	}


	public String getSalaryRange() {
		return salaryRange;
	}


	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}


	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}




	public String getJobLevel() {
		return jobLevel;
	}


	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}


	public JobStatus getStatus() {
		return status;
	}


	public void setStatus(JobStatus status) {
		this.status = status;
	}
}
