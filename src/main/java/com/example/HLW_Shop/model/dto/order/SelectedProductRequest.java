package com.example.HLW_Shop.model.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelectedProductRequest {
    String productId;
    int quantity;
    String userId;
}
