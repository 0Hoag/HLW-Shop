package com.example.HLW_Shop.config.dtb.mongoDB;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.example.HLW_Shop.repository.mongo.product",
        mongoTemplateRef = "productMongoTemplate"
)
public class ProductMongoConfig {
}