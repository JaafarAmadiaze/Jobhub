package com.example.demo.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;// "RECRUITER" or "CANDIDATE"

    @OneToMany(mappedBy = "recruiter", fetch = FetchType.LAZY)
    private List<Job> postedJobs;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Review> writtenReviews;

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return this.id;
    }
    public String getPassword() {
        return this.password;
    }
    public String getRole() {
        return this.role;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

