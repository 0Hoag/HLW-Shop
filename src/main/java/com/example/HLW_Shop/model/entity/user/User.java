package com.example.HLW_Shop.model.entity.user;

import com.example.HLW_Shop.model.entity.order.CartItem;
import com.example.HLW_Shop.model.entity.order.Orders;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    @Column(name = "username", unique = true, columnDefinition = "TEXT")
    String username;

    @Column(name = "first_name", columnDefinition = "TEXT")
    String firstName;

    @Column(name = "last_name", columnDefinition = "TEXT")
    String lastName;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    String password;

    @Column(name = "email", unique = true, columnDefinition = "TEXT")
    String email;

    @Column(name = "email_verified", nullable = false)
    boolean emailVerified = false;

    @Column(name = "image_url")
    String images;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    Set<CartItem> cartItem;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    Set<Orders> orders;

    @Override
    public String toString() {
        return "User{" + "userId='"
                + userId + '\'' + ", username='"
                + username + '\'' + ", firstName='"
                + firstName + '\'' + ", lastName='"
                + lastName + '\'' + ", email='"
                + email + '\'' + ", emailVerified="
                + emailVerified + ", rolesCount="
                + images + ", images="
                + (roles != null ? roles.size() : 0) + ", cartItemsCount="
                + (cartItem != null ? cartItem.size() : 0) + ", ordersCount="
                + (orders != null ? orders.size() : 0) + ", imagesCount=" + '}';
    }
}
