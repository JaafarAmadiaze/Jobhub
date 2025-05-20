package com.example.demo.services;

import com.example.demo.entities.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    List<Review> getAllReviews();
    Review getReviewById(Long id);
    Review createReview(Review review);
    Review updateReview(Long id, Review updatedReview);
    void deleteReview(Long id);
}
