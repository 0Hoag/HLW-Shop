package com.example.HLW_Shop.model.entity.order;

import com.example.HLW_Shop.model.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class SelectedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String selectedId;

    int quantity;
    String productId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    User user;
}
