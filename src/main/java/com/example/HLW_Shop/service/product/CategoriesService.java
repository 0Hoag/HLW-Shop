package com.example.HLW_Shop.service.product;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.product.CategoryMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.request.CategoryRequest;
import com.example.HLW_Shop.model.dto.product.response.CategoryResponse;
import com.example.HLW_Shop.model.dto.product.request.CategoryUpdateRequest;
import com.example.HLW_Shop.model.entity.product.Category;
import com.example.HLW_Shop.repository.mongo.product.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoriesService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse toCreateCategories(CategoryRequest request) {
        var categories = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(categories));
    }

    public CategoryResponse getCategories(String categoriesId) {
        var categories = categoryRepository.findById(categoriesId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        return categoryMapper.toCategoryResponse(categories);
    }

    public PageResponse<CategoryResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> pageData = categoryRepository.findAll(pageable);

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(categoryMapper::toCategoryResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteCategory(String categoriesId) {
        var categories = categoryRepository.findById(categoriesId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        categoryRepository.deleteById(categories.getCategoriesId());
    }

    public Boolean updateCategories(String categoriesId, CategoryUpdateRequest request) {
        var categories = categoryRepository.findById(categoriesId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        categoryMapper.updateCategory(categories, request);

        return true;
    }
}
