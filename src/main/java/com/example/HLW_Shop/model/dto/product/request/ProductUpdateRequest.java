package com.example.HLW_Shop.model.dto.product.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String author;
    String name;
    String address;
    double listedPrice;
    double price;
    int quantity;
    int stockQuantity;
    String description;
    Set<String> image;

    List<String> categoryIds;
}
