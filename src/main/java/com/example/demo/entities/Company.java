package com.example.demo.entities;

import jakarta.persistence.*;


import java.util.List;


@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    @Column(length = 1000)
    private String description;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Job> jobs;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Review> reviews;

    // 🔹 Constructeur vide requis par Hibernate
    public Company() {}

    // 🔹 Constructeur pratique sans id/lists
    public Company(String name, String industry) {
        this.name = name;
        this.industry = industry;
    }

    // ✅ Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    // ✅ Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
