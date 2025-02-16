package com.example.HLW_Shop.model.dto.review;



import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyUpdateRequest {
    String reviewId;
    String message;
}
