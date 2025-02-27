package com.example.HLW_Shop.model.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoveSelectedProductWithOrdersWithUserRequest {
    Set<String> orderId;
}
