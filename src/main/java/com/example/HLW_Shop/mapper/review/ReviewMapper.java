package com.example.HLW_Shop.mapper.review;

import com.example.HLW_Shop.model.dto.review.CreateReviewRequest;
import com.example.HLW_Shop.model.dto.review.ReviewResponse;
import com.example.HLW_Shop.model.dto.review.ReviewUpdateRequest;
import com.example.HLW_Shop.model.entity.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(CreateReviewRequest request);

    ReviewResponse toReviewResponse(Review entity);

    void UpdateReview(@MappingTarget Review entity, ReviewUpdateRequest request);

}
