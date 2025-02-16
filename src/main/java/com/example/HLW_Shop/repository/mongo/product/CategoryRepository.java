package com.example.HLW_Shop.repository.mongo.product;

import com.example.HLW_Shop.model.entity.product.Category;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByParentCategoryId(String parentCategoryId);
}
