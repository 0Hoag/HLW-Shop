package com.example.HLW_Shop.controller.product;

import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.request.CategoryRequest;
import com.example.HLW_Shop.model.dto.product.request.CategoryUpdateRequest;
import com.example.HLW_Shop.model.dto.product.response.CategoryResponse;
import com.example.HLW_Shop.service.product.CategoriesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoriesController {
    CategoriesService categoriesService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategories(@RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .result(categoriesService.toCreateCategories(request))
                .build();
    }

    @GetMapping("/getCategories/{categoriesId}")
    public ApiResponse<CategoryResponse> getCategories(@PathVariable String categoriesId) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .result(categoriesService.getCategories(categoriesId))
                .build();
    }

    @GetMapping("/getAllCategories")
    public ApiResponse<PageResponse<CategoryResponse>> getCategories(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size)  {
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(1000)
                .result(categoriesService.getAllCategories(page, size))
                .build();
    }

    @DeleteMapping("/deleteCategories/{categoriesId}")
    public ApiResponse<Void> deleteCategories(@PathVariable String categoriesId) {
        categoriesService.deleteCategory(categoriesId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete categories success!")
                .build();
    }

    @PutMapping("/updateCategories/{categoriesId}")
    public ApiResponse<Boolean> updateCategories(@PathVariable String categoriesId, @RequestBody CategoryUpdateRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(categoriesService.updateCategories(categoriesId, request))
                .build();
    }
}
