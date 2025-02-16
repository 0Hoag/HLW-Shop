package com.example.HLW_Shop.model.dto.user;

import com.example.HLW_Shop.model.dto.order.CartItemResponse;
import com.example.HLW_Shop.model.dto.order.OrdersResponse;
import com.example.HLW_Shop.model.dto.role.RoleResponse;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;

    String username;
    String firstName;
    String lastName;
    Boolean noPassword;

    String email;
    boolean emailVerified;
    String images;

    Set<RoleResponse> roles;
    Set<CartItemResponse> cartItem;
    Set<OrdersResponse> orders;
}
