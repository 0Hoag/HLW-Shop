package com.example.HLW_Shop.config.dtb.mongoDB;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoDBConfig {
    @Value("${spring.data.mongodb.product.uri}")
    private String productMongoUri;

    @Value("${spring.data.mongodb.review.uri}")
    private String reviewMongoUri;

    @Primary
    @Bean(name = "productMongoTemplate")
    public MongoTemplate productMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(
                MongoClients.create(productMongoUri),
                "product_db"
        ));
    }

    @Bean(name = "reviewMongoTemplate")
    public MongoTemplate reviewMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(
                MongoClients.create(reviewMongoUri),
                "review_db"
        ));
    }
}