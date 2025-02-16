package com.example.HLW_Shop.mapper.order;


import com.example.HLW_Shop.model.dto.order.CreateOrdersRequest;
import com.example.HLW_Shop.model.dto.order.OrdersResponse;
import com.example.HLW_Shop.model.dto.order.SelectedProductResponse;
import com.example.HLW_Shop.model.dto.order.UpdateOrdersRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.entity.order.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    @Mapping(target = "selectedProducts", ignore = true)
    Orders toOrders(CreateOrdersRequest request);

    @Mapping(target = "selectedProducts", ignore = true)
    OrdersResponse toOrdersResponse(Orders orders);

    void updateOrder(@MappingTarget Orders orders, UpdateOrdersRequest request);

    @Named("mapProductIdToProductResponse")
    default ProductResponse mapProductIdToProductResponse(String productId) {
        return ProductResponse.builder().productId(productId).build();
    }

    // You may need to adjust or remove this mapping based on your entity structure
    @Named("mapSelectedProductResponse")
    default SelectedProductResponse mapSelectedProductResponse(String selectedId) {
        return SelectedProductResponse.builder().selectedId(selectedId).build();
    }
}
