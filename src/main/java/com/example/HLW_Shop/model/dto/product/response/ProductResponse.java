package com.example.HLW_Shop.model.dto.product.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String productId;
    String authorId; // author id
    String name;
    String address;
    double listedPrice;
    double price;
    int quantity;
    int stockQuantity;
    String description;
    Set<String> image;

    List<String> categoriesId;
}
