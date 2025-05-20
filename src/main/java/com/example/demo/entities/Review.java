package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    // Setter manquant pour le binding
    public void setCompany(Company company) {
        this.company = company;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    // Optionnel si tu as besoin de les appeler individuellement :
    public Long getId() { return id; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public User getAuthor() { return author; }
    public Company getCompany() { return company; }
    public void setId(Long id) { this.id = id; }
    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment;}
}
