package com.example.HLW_Shop.model.entity.review;


import com.example.HLW_Shop.model.enums.CheckPoint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("review")
@CompoundIndex(def = "{'userId': 1, 'productId': 1}", unique = true)
public class Review {
    @MongoId
    @Field(name = "review_id")
    String reviewId;
    String comment;

    @Min(1)
    @Max(5)
    int rating;

    @Field(name = "user_id")
    String userId;

    @Field(name = "product_id")
    String productId;

    @Field(name = "create_at")
    LocalDate createAt;

    @Field(name = "check_point")
    CheckPoint checkPoint;

    @Field(name = "update_at")
    LocalDate updateAt;
}
