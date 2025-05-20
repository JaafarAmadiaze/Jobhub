package com.example.demo.services;

import com.example.demo.entities.Review;
import com.example.demo.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service


public class ReviewServiceImpl implements ReviewService {
    private  ReviewRepository reviewRepository;

    // ✅ Injection par constructeur
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long id, Review updatedReview) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));


        return reviewRepository.save(existing);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
