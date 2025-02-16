package com.example.HLW_Shop.model.entity.review;


import com.example.HLW_Shop.model.enums.CheckPoint;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("reply")
public class Reply {
    @MongoId
    @Field(name = "reply_id")
    String replyId;

    @Field(name = "user_id")
    String userId;

    @Field(name = "review_id")
    String reviewId;
    String message;

    @Field(name = "create_at")
    LocalDate createAt;

    @Field(name = "check_point")
    CheckPoint checkPoint = CheckPoint.None_Update;

    @Field(name = "update_at")
    LocalDate updateAt;

    @Field("parent_reply_id")
    String parentReplyId;
}
