package com.example.HLW_Shop.model.entity.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("category")
public class Category {
    @MongoId
    @Field(name = "categories_Id")
    String categoriesId;

    String name;
    String description;

    @Field("parent_category_id")
    String parentCategoryId;
}
