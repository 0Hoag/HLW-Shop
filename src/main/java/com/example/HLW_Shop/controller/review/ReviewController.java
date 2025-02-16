package com.example.HLW_Shop.controller.review;

import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.review.CreateReviewRequest;
import com.example.HLW_Shop.model.dto.review.ReviewResponse;
import com.example.HLW_Shop.model.dto.review.ReviewUpdateRequest;
import com.example.HLW_Shop.service.review.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/create")
    public ApiResponse<ReviewResponse> createReview(@RequestBody CreateReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .result(reviewService.toCreateReview(request))
                .build();
    }

    @GetMapping("/getReview/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(@PathVariable("reviewId") String reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .result(reviewService.getReview(reviewId))
                .build();
    }

    @GetMapping("/getAllReviewByUserId/{userId}")
    public ApiResponse<PageResponse<ReviewResponse>> getAllReviewByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .code(1000)
                .result(reviewService.getAllReviewByUserId(userId, page, size))
                .build();
    }

    @GetMapping("/getAllReviewByProductId/{productId}")
    public ApiResponse<PageResponse<ReviewResponse>> getAllReviewByProductId(
            @PathVariable String productId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .code(1000)
                .result(reviewService.getAllReviewByProductId(productId, page, size))
                .build();
    }

    @GetMapping("/getAllReview")
    public ApiResponse<PageResponse<ReviewResponse>> getAllReview(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .code(1000)
                .result(reviewService.getAllReview(page, size))
                .build();
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable("reviewId") String reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete review success!")
                .build();
    }

    @PutMapping("/updateReview/{reviewId}")
    public ApiResponse<Boolean> updateProduct(@PathVariable String reviewId, @RequestBody ReviewUpdateRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(reviewService.updateReview(reviewId, request))
                .build();
    }
}
