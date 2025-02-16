package com.example.HLW_Shop.repository.postgres;

import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedProductRepository extends JpaRepository<SelectedProduct, String> {}
