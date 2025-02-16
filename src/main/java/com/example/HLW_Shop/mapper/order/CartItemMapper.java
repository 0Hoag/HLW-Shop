package com.example.HLW_Shop.mapper.order;


import com.example.HLW_Shop.mapper.product.ProductMapper;
import com.example.HLW_Shop.model.dto.order.CartItemRequest;
import com.example.HLW_Shop.model.dto.order.CartItemResponse;
import com.example.HLW_Shop.model.dto.order.UpdateCartItemRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.entity.order.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mappings({@Mapping(target = "productId", ignore = true)})
    CartItem toCartItem(CartItemRequest request);

    @Mappings({@Mapping(source = "productId", target = "productId", qualifiedByName = "mapProductIdToProductResponse")})
    CartItemResponse toCartItemResponse(CartItem cartItem);

    void updateCartItem(@MappingTarget CartItem cartItem, UpdateCartItemRequest request);

    @Named("mapProductIdToProductResponse")
    default ProductResponse mapProductIdToProductResponse(String productId) {
        return ProductResponse.builder().productId(productId).build();
    }
}
