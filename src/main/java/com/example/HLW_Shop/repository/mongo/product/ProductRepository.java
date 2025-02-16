package com.example.HLW_Shop.repository.mongo.product;

import com.example.HLW_Shop.model.entity.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
