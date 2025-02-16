package com.example.HLW_Shop.model.dto.order;

import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelectedProductResponse {
    String selectedId;
    int quantity;
    ProductResponse productId;
}
