package com.example.HLW_Shop.service.review;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.review.ReviewMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.review.CreateReviewRequest;
import com.example.HLW_Shop.model.dto.review.ReviewResponse;
import com.example.HLW_Shop.model.dto.review.ReviewUpdateRequest;
import com.example.HLW_Shop.model.entity.review.Reply;
import com.example.HLW_Shop.model.entity.review.Review;
import com.example.HLW_Shop.model.enums.CheckPoint;
import com.example.HLW_Shop.repository.mongo.product.ProductRepository;
import com.example.HLW_Shop.repository.mongo.review.ReplyRepository;
import com.example.HLW_Shop.repository.mongo.review.ReviewRepository;
import com.example.HLW_Shop.repository.postgres.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewMapper reviewMapper;
    ReplyRepository replyRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ReviewRepository reviewRepository;

    public ReviewResponse toCreateReview(CreateReviewRequest request) {
        var review = reviewMapper.toReview(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepository.findById(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        productRepository.findById(review.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (reviewRepository.findByUserIdAndProductId(review.getUserId(), review.getProductId()).isPresent()) {
            throw new AppException(ErrorCode.REVIEW_IT_EXISTED);
        }

        review.setCheckPoint(CheckPoint.None_Update);
        review.setUserId(authentication.getName());
        review.setCreateAt(LocalDate.now());
        review.setUpdateAt(LocalDate.now());

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    public ReviewResponse getReview(String reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        return reviewMapper.toReviewResponse(review);
    }

    public PageResponse<ReviewResponse> getAllReviewByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> pageData = reviewRepository.findAll(pageable);

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(reviewRepository.findByUserId(userId)
                        .stream().map(reviewMapper::toReviewResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<ReviewResponse> getAllReviewByProductId(String productId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> pageData = reviewRepository.findAll(pageable);

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(reviewRepository.findByProductId(productId)
                        .stream().map(reviewMapper::toReviewResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<ReviewResponse> getAllReview(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> pageData = reviewRepository.findAll(pageable);

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(reviewRepository.findAll()
                        .stream().map(reviewMapper::toReviewResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteReview(String reviewId) {
        var review = reviewRepository.findById(reviewId).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        replyRepository.deleteAllByReviewId(review.getReviewId());

        reviewRepository.deleteById(review.getReviewId());
    }

    public Boolean updateReview(String reviewId, ReviewUpdateRequest request) {
        var review = reviewRepository.findById(reviewId).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
        reviewMapper.UpdateReview(review, request);

        review.setCheckPoint(CheckPoint.Update);
        review.setUpdateAt(LocalDate.now());

        reviewRepository.save(review);

        return true;
    }
}
