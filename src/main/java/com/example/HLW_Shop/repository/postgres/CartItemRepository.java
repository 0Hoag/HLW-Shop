package com.example.HLW_Shop.repository.postgres;


import com.example.HLW_Shop.model.entity.order.CartItem;
import com.example.HLW_Shop.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByUser(User user);
}
