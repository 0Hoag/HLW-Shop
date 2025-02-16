package com.example.HLW_Shop.mapper.product;

import com.example.HLW_Shop.model.dto.product.request.CreateProductRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.dto.product.request.ProductUpdateRequest;
import com.example.HLW_Shop.model.dto.product.request.UpdateStockQuantityProductRequest;
import com.example.HLW_Shop.model.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductRequest request);

    ProductResponse toProductResponse(Product entity);

    void updateStockQuantityProduct (@MappingTarget Product product, UpdateStockQuantityProductRequest request);

    void updateProduct (@MappingTarget Product product, ProductUpdateRequest request);
}
