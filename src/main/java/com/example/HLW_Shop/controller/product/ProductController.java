package com.example.HLW_Shop.controller.product;

import com.cloudinary.Api;
import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.request.CreateProductRequest;
import com.example.HLW_Shop.model.dto.product.request.ProductUpdateRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.toCreateProduct(request))
                .build();
    }

    @GetMapping("/getProduct/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable String productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.getProduct(productId))
                .build();
    }

    @GetMapping("/getAllProduct")
    public ApiResponse<PageResponse<ProductResponse>> getAllProduct(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(1000)
                .result(productService.getAllProduct(page, size))
                .build();
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable @Valid String productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete product success!")
                .build();
    }

    @PostMapping("/uploadImageProduct/{productId}")
    ApiResponse<Void> uploadImageUserProfile(@PathVariable String productId, @RequestPart("image") MultipartFile file)
            throws SQLException, IOException {
        productService.uploadImageProduct(productId, file);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Update image photo success!")
                .build();
    }

    @PutMapping("/update/{productId}")
    public ApiResponse<Boolean> updateProduct(@PathVariable String productId, @RequestBody ProductUpdateRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(productService.updateProduct(productId, request))
                .build();
    }

}
