package com.example.HLW_Shop.service.review;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.review.ReplyMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.review.*;
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
public class ReplyService {
    ReplyRepository replyRepository;
    ReplyMapper replyMapper;
    UserRepository userRepository;
    ReviewRepository reviewRepository;

    public ReplyResponse toCreateReply(CreateReplyRequest request) {
        var reply = replyMapper.toReply(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepository.findById(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        reviewRepository.findById(request.getReviewId()).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        reply.setCheckPoint(CheckPoint.None_Update);
        reply.setUserId(authentication.getName());
        reply.setCreateAt(LocalDate.now());
        reply.setUpdateAt(LocalDate.now());

        return replyMapper.toReplyResponse(replyRepository.save(reply));
    }

    public ReplyResponse getReply(String replyId) {
        var reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new AppException(ErrorCode.REPLY_NOT_EXISTED));

        return replyMapper.toReplyResponse(reply);
    }

    public PageResponse<ReplyResponse> getAllReplyByReviewId(String replyId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Reply> pageData = replyRepository.findAll(pageable);

        return PageResponse.<ReplyResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(replyRepository.findByReviewId(replyId)
                        .stream().map(replyMapper::toReplyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<ReplyResponse> getAllParentReplyId(String parentReplyId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Reply> pageData = replyRepository.findAll(pageable);

        return PageResponse.<ReplyResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(replyRepository.findByParentReplyId(parentReplyId)
                        .stream().map(replyMapper::toReplyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<ReplyResponse> getAllReply(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Reply> pageData = replyRepository.findAll(pageable);

        return PageResponse.<ReplyResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .data(replyRepository.findAll()
                        .stream().map(replyMapper::toReplyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteReply(String replyId) {
        var reply = replyRepository.findById(replyId).orElseThrow(() -> new AppException(ErrorCode.REPLY_NOT_EXISTED));

        reviewRepository.findById(reply.getReviewId()).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
        replyRepository.deleteById(replyId);
    }

    public Boolean updateReply(String replyId, ReplyUpdateRequest request) {
        var reply = replyRepository.findById(replyId).orElseThrow(() -> new AppException(ErrorCode.REPLY_NOT_EXISTED));
        replyMapper.UpdateReply(reply, request);

        reply.setCheckPoint(CheckPoint.Update);
        reply.setUpdateAt(LocalDate.now());

        replyRepository.save(reply);

        return true;
    }
}
