package com.example.HLW_Shop.model.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInformationBasicResponse {
    String userId;
    String username;
    String firstName;
    String lastName;
    String images;
}
