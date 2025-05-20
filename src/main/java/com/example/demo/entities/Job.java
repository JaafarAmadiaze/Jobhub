package com.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private Double salary;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private com.example.demo.entities.Category category;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<Application> applications;

    public Job() {}

    public Job(Long id, String title, String description, String location, Double salary, LocalDateTime createdAt, User recruiter, Company company, com.example.demo.entities.Category category, List<Application> applications) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.createdAt = createdAt;
        this.recruiter = recruiter;
        this.company = company;
        this.category = category;
        this.applications = applications;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public Double getSalary() { return salary; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getRecruiter() { return recruiter; }
    public Company getCompany() { return company; }
    public com.example.demo.entities.Category getCategory() { return category; }
    public List<Application> getApplications() { return applications; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRecruiter(User recruiter) { this.recruiter = recruiter; }
    public void setCompany(Company company) { this.company = company; }
    public void setCategory(com.example.demo.entities.Category category) { this.category = category; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
}
