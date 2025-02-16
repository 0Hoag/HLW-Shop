package com.example.HLW_Shop.repository.mongo.review;

import com.example.HLW_Shop.model.entity.review.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
    void deleteAllByReviewId(String reviewId);

    List<Reply> findByReviewId(String reviewId);

    List<Reply> findByParentReplyId(String parentReplyId);
}
