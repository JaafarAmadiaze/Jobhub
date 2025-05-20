package com.example.demo.controllers;

import com.example.demo.entities.Review;
import com.example.demo.entities.User;
import com.example.demo.services.ReviewService;
import com.example.demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }
    @PostMapping("/submitReview")
    public String submitReview(@ModelAttribute Review newReview) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User author = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        newReview.setAuthor(author);

        if (newReview.getCompany() == null || newReview.getCompany().getId() == null) {
            throw new RuntimeException("ID de l'entreprise manquant");
        }

        reviewService.createReview(newReview);

        return "redirect:/company/" + newReview.getCompany().getId();
    }

}
