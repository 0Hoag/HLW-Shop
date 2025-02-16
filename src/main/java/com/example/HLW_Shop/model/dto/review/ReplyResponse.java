package com.example.HLW_Shop.model.dto.review;

import com.example.HLW_Shop.model.enums.CheckPoint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyResponse {
    String replyId;
    String userId;
    String reviewId;
    String message;
    LocalDate createAt;
    CheckPoint checkPoint;
    LocalDate updateAt;
    String parentReplyId;
}
