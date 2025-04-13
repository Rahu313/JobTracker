package com.jobtracker.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public User() {
		super();
	}

	public User(Long id, String name, String email, String password, String role, String authProvider,
			String profilePic, LocalDateTime createdAt, String resumeFile, String skills, String companyName,
			String companyWebsite) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.authProvider = authProvider;
		this.profilePic = profilePic;
		this.createdAt = createdAt;
		this.resumeFile = resumeFile;
		this.skills = skills;
		this.companyName = companyName;
		this.companyWebsite = companyWebsite;
	}

	public User(Long id, String name, String email, String password, String role, String authProvider,
			String profilePic, LocalDateTime createdAt, String resumeFile, String skills, String companyName) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.authProvider = authProvider;
		this.profilePic = profilePic;
		this.createdAt = createdAt;
		this.resumeFile = resumeFile;
		this.skills = skills;
		this.companyName = companyName;
	}

	public User(Long id, String name, String email, String password, String role, String authProvider,
			String profilePic, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.authProvider = authProvider;
		this.profilePic = profilePic;
		this.createdAt = createdAt;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@JsonIgnore
	private String password; // Nullable for Google sign-in

	private String role; // "applicant" or "poster"

	@Column(name = "auth_provider")
	private String authProvider; // e.g., "google", "local"

	@Column(name = "profile_pic")
	private String profilePic;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	private String resumeFile; // Instead of file
	private String skills;
	private String companyName;
	private String companyWebsite;

	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getResumeFile() {
		return resumeFile;
	}

	public void setResumeFile(String resumeFile) {
		this.resumeFile = resumeFile;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	// Getters and Setters
}
