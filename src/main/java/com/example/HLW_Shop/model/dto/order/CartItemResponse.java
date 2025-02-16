package com.example.HLW_Shop.model.dto.order;

import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String cartItemId;
    int quantity;
    ProductResponse productId;
}
