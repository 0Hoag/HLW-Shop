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
public class CreateProductRequest {
    String name;
    String authorId; // author id
    String address;
    double listedPrice;
    double price;
    int quantity;
    int stockQuantity;
    String description;
    Set<String> image;

    List<String> categoriesId;
}
