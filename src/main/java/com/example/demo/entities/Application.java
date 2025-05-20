package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appliedAt;
    private String status;
    private String skills;
    private String certifications;
    private String languages;
    private String projects;
    private String hobbies;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private User candidate;


    public void setJob(Job job) {
        this.job = job;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Job getJob() {
        return this.job;
    }

    public User getCandidate() {
        return this.candidate;
    }
    public LocalDateTime getAppliedAt() {
        return this.appliedAt;
    }

    public String getStatus() {
        return this.status;
    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSkills() {
        return skills;
    }
    public void setSkills(String skills) {
        this.skills = skills;
    }
    public String getCertifications() {
        return certifications;
    }
    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
    public String getLanguages() {
        return languages;
    }
    public void setLanguages(String languages) {
        this.languages = languages;
    }
    public String getProjects() {
        return projects;
    }
    public void setProjects(String projects) {
        this.projects = projects;
    }
    public String getHobbies() {
        return hobbies;
    }
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

}
