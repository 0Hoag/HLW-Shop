package com.example.HLW_Shop.controller.review;

import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.review.*;
import com.example.HLW_Shop.service.review.ReplyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyController {
    ReplyService replyService;

    @PostMapping("/create")
    public ApiResponse<ReplyResponse> createReply(@RequestBody CreateReplyRequest request) {
        return ApiResponse.<ReplyResponse>builder()
                .code(1000)
                .result(replyService.toCreateReply(request))
                .build();
    }

    @GetMapping("/getReply/{replyId}")
    public ApiResponse<ReplyResponse> getReview(@PathVariable("replyId") String reviewId) {
        return ApiResponse.<ReplyResponse>builder()
                .code(1000)
                .result(replyService.getReply(reviewId))
                .build();
    }

    @GetMapping("/getAllReplyByReviewId/{reviewId}")
    public ApiResponse<PageResponse<ReplyResponse>> getAllReplyByReviewId(
            @PathVariable String reviewId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReplyResponse>>builder()
                .code(1000)
                .result(replyService.getAllReplyByReviewId(reviewId, page, size))
                .build();
    }

    @GetMapping("/getAllReplyByParentReplyId/{parentReplyId}")
    public ApiResponse<PageResponse<ReplyResponse>> getAllReplyByParentReplyId(
            @PathVariable String parentReplyId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReplyResponse>>builder()
                .code(1000)
                .result(replyService.getAllParentReplyId(parentReplyId, page, size))
                .build();
    }

    @GetMapping("/getAllReply")
    public ApiResponse<PageResponse<ReplyResponse>> getAllReply(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ReplyResponse>>builder()
                .code(1000)
                .result(replyService.getAllReply(page, size))
                .build();
    }

    @DeleteMapping("/deleteReply/{replyId}")
    public ApiResponse<Void> deleteReview(@PathVariable("replyId") String replyId) {
        replyService.deleteReply(replyId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete reply success!")
                .build();
    }

    @PutMapping("/updateReply/{replyId}")
    public ApiResponse<Boolean> updateProduct(@PathVariable String replyId, @RequestBody ReplyUpdateRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(replyService.updateReply(replyId, request))
                .build();
    }
}
