package com.example.HLW_Shop.mapper.product;


import com.example.HLW_Shop.model.dto.product.request.CategoryRequest;
import com.example.HLW_Shop.model.dto.product.response.CategoryResponse;
import com.example.HLW_Shop.model.dto.product.request.CategoryUpdateRequest;
import com.example.HLW_Shop.model.entity.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category entity);

    void updateCategory(@MappingTarget Category profile, CategoryUpdateRequest request);
}
