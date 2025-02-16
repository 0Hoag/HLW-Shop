package com.example.HLW_Shop.mapper.user;

import com.example.HLW_Shop.mapper.order.CartItemMapper;
import com.example.HLW_Shop.mapper.order.SelectedProductMapper;
import com.example.HLW_Shop.model.dto.user.UpdateInformationRequest;
import com.example.HLW_Shop.model.dto.user.UserCreateRequest;
import com.example.HLW_Shop.model.dto.user.UserResponse;
import com.example.HLW_Shop.model.dto.user.UserUpdateRequest;
import com.example.HLW_Shop.model.entity.order.CartItem;
import com.example.HLW_Shop.model.entity.order.Orders;
import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import com.example.HLW_Shop.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {CartItemMapper.class, SelectedProductMapper.class})
public interface UserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateInformationUser(@MappingTarget User user, UpdateInformationRequest request);

    default Set<CartItem> map(Collection<String> cartItemId) {
        return cartItemId.stream()
                .map(id -> CartItem.builder().cartItemId(id).build())
                .collect(Collectors.toSet());
    }

    default Set<SelectedProduct> mapSelectedProducts(Set<String> selectedProductId) {
        return selectedProductId.stream()
                .map(id -> SelectedProduct.builder().selectedId(id).build())
                .collect(Collectors.toSet());
    }

    default Set<Orders> mapOrders(Set<String> orderId) {
        return orderId.stream().map(id -> Orders.builder().orderId(id).build()).collect(Collectors.toSet());
    }
}
