package com.example.HLW_Shop.model.dto.review;



import com.example.HLW_Shop.model.entity.review.Reply;
import com.example.HLW_Shop.model.enums.CheckPoint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String reviewId;
    String comment;
    int rating;
    String userId;
    String productId;
    LocalDate createAt;
    CheckPoint checkPoint;
    LocalDate updateAt;
}
