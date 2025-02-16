package com.example.HLW_Shop.mapper.order;


import com.example.HLW_Shop.model.dto.order.SelectedProductRequest;
import com.example.HLW_Shop.model.dto.order.SelectedProductResponse;
import com.example.HLW_Shop.model.dto.order.UpdateSelectedProductRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SelectedProductMapper {
    @Mappings({@Mapping(target = "productId", ignore = true)})
    SelectedProduct toSelectedProduct(SelectedProductRequest request);

    @Mappings({@Mapping(source = "productId", target = "productId", qualifiedByName = "mapProductIdToProductResponse")})
    SelectedProductResponse toSelectedProductResponse(SelectedProduct selectedProduct);

    void updateSelectedProduct(@MappingTarget SelectedProduct selectedProduct, UpdateSelectedProductRequest request);

    @Named("mapProductIdToProductResponse")
    default ProductResponse mapProductIdToProductResponse(String productId) {
        return ProductResponse.builder().productId(productId).build();
    }
}
