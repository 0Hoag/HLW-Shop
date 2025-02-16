package com.example.HLW_Shop.controller.product;

import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.request.CreateAuthorRequest;
import com.example.HLW_Shop.model.dto.product.request.ProductUpdateRequest;
import com.example.HLW_Shop.model.dto.product.request.UpdateAuthorRequest;
import com.example.HLW_Shop.model.dto.product.response.AuthorResponse;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.service.product.AuthorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController {
    AuthorService authorService;

    @PostMapping
    public ApiResponse<AuthorResponse> createAuthor(@RequestBody @Valid CreateAuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .code(1000)
                .result(authorService.toCreateAuthor(request))
                .build();
    }

    @GetMapping("/getAuthor/{authorId}")
    public ApiResponse<AuthorResponse> getAuthor(@PathVariable String authorId) {
        return ApiResponse.<AuthorResponse>builder()
                .code(1000)
                .result(authorService.toGetAuthor(authorId))
                .build();
    }

    @GetMapping("/getAllAuthor")
    public ApiResponse<PageResponse<AuthorResponse>> getAllAuthor(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<AuthorResponse>>builder()
                .code(1000)
                .result(authorService.toGetAllAuthor(page, size))
                .build();
    }

    @DeleteMapping("/deleteAuthor/{authorId}")
    public ApiResponse<Void> deleteAuthor(@PathVariable @Valid String authorId) {
        authorService.deleteAuthor(authorId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete author success!")
                .build();
    }

    @PutMapping("/update/{authorId}")
    public ApiResponse<Boolean> updateAuthor(@PathVariable String authorId, @RequestBody UpdateAuthorRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(authorService.updateAuthor(authorId, request))
                .build();
    }

}
