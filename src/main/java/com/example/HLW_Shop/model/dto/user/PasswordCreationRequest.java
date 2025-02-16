package com.example.HLW_Shop.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordCreationRequest {
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
}
