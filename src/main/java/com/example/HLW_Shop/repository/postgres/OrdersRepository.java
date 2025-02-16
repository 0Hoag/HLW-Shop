package com.example.HLW_Shop.repository.postgres;

import com.example.HLW_Shop.model.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {}
