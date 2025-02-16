package com.example.HLW_Shop.mapper.product;


import com.example.HLW_Shop.model.dto.product.response.AuthorResponse;
import com.example.HLW_Shop.model.dto.product.request.CreateAuthorRequest;
import com.example.HLW_Shop.model.dto.product.request.UpdateAuthorRequest;
import com.example.HLW_Shop.model.entity.product.Author;
import com.example.HLW_Shop.model.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
//    @Mapping(target = "product", source = "product", qualifiedByName = "mapIdsToProducts")
    Author toAuthor(CreateAuthorRequest request);

    AuthorResponse toAuthorResponse(Author entity);

    void updateAuthor(@MappingTarget Author author, UpdateAuthorRequest request);

//    @Named("mapIdsToProducts")
//    default Set<Product> mapIdsToProducts(Set<String> productIds) {
//        if (productIds == null) {
//            return null;
//        }
//        return productIds.stream()
//                .map(id -> {
//                    Product product = new Product();
//                    product.setProductId(id);
//                    return product;
//                })
//                .collect(Collectors.toSet());
//    }

    default String mapProductToId(Product product) {
        return product != null ? product.getProductId() : null;
    }
}