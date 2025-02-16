package com.example.HLW_Shop.model.dto.product.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String categoriesId;

    String name;
    String description;

    @Field("parent_category_id")
    String parentCategoryId;
}
