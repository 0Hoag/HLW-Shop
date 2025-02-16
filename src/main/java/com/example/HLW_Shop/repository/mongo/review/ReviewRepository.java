package com.example.HLW_Shop.repository.mongo.review;

import com.example.HLW_Shop.model.entity.review.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findByUserIdAndProductId(String userId, String productId);
    List<Review> findByUserId(String userId);
    List<Review> findByProductId(String productId);
}
