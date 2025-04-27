package com.jobtracker.dto;

import java.time.LocalDateTime;

public class ApplicationDTO {
    public ApplicationDTO(Long id, String jobTitle, String companyName, String linkedinUrl, Double currentCtc,
			Double expectedCtc, String status, String resumeFile,LocalDateTime appliedAt,String salaryRange) {
		super();
		this.id = id;
		this.jobTitle = jobTitle;
		this.companyName = companyName;
		this.linkedinUrl = linkedinUrl;
		this.currentCtc = currentCtc;
		this.expectedCtc = expectedCtc;
		this.status = status;
		this.resumeFile = resumeFile;
		this.appliedAt=appliedAt;
		this.salaryRange=salaryRange;
	}
	private Long id;
    private String jobTitle;
    private String companyName;
    private String linkedinUrl;
    private Double currentCtc;
    private Double expectedCtc;
    private String status;
    private String resumeFile;
    private LocalDateTime appliedAt;
    private String salaryRange;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLinkedinUrl() {
		return linkedinUrl;
	}
	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}
	public Double getCurrentCtc() {
		return currentCtc;
	}
	public void setCurrentCtc(Double currentCtc) {
		this.currentCtc = currentCtc;
	}
	public Double getExpectedCtc() {
		return expectedCtc;
	}
	public void setExpectedCtc(Double expectedCtc) {
		this.expectedCtc = expectedCtc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResumeFile() {
		return resumeFile;
	}
	public void setResumeFile(String resumeFile) {
		this.resumeFile = resumeFile;
	}
	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}
	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}
	public String getSalaryRange() {
		return salaryRange;
	}
	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}
    // getters, setters, constructor
}
