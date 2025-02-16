package com.example.HLW_Shop.model.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCartItemRequest {
    Set<String> cartItemId;
}
