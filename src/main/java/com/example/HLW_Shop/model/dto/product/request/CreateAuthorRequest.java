package com.example.HLW_Shop.model.dto.product.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAuthorRequest {
    String name;
    Set<String> product;
}
