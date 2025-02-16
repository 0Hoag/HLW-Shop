package com.example.HLW_Shop.mapper.review;

import com.example.HLW_Shop.model.dto.review.*;
import com.example.HLW_Shop.model.entity.review.Reply;
import com.example.HLW_Shop.model.entity.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReplyMapper {
    Reply toReply(CreateReplyRequest request);

    ReplyResponse toReplyResponse(Reply entity);

    void UpdateReply(@MappingTarget Reply entity, ReplyUpdateRequest request);
}
