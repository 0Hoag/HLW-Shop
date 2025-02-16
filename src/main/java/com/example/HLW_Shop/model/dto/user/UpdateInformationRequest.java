package com.example.HLW_Shop.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInformationRequest {
    String firstName;
    String lastName;
    String email;
}
