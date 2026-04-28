package com.carrental.controller;

import com.carrental.dto.ApiResponse;
import com.carrental.dto.CreateReviewRequest;
import com.carrental.dto.ReviewDTO;
import com.carrental.service.CurrentUserService;
import com.carrental.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody CreateReviewRequest request,
                                                                Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        ReviewDTO review = reviewService.createReview(userId, request);
        return ResponseEntity.ok(ApiResponse.success(review, "Review created successfully"));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByCar(@PathVariable Long carId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByCar(carId)));
    }
}
