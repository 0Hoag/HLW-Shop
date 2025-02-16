package com.example.HLW_Shop.model.entity.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("product")
public class Product {
    @MongoId
    @Field(name = "product_id")
    String productId;

    @Field(name = "author_id")
    String authorId;

    String name;
    String address;

    @Field(name = "listed_price")
    double listedPrice;
    double price;
    int quantity;

    @Field(name = "stock_quantity")
    int stockQuantity;
    String description;
    Set<String> image;

    @Field(name = "categories_Id")
    List<String> categoriesId;
}
