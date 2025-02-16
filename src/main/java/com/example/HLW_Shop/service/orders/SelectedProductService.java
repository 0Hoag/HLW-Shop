package com.example.HLW_Shop.service.orders;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.order.SelectedProductMapper;
import com.example.HLW_Shop.model.dto.order.*;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import com.example.HLW_Shop.model.entity.user.User;
import com.example.HLW_Shop.repository.postgres.CartItemRepository;
import com.example.HLW_Shop.repository.postgres.OrdersRepository;
import com.example.HLW_Shop.repository.postgres.SelectedProductRepository;
import com.example.HLW_Shop.repository.postgres.UserRepository;
import com.example.HLW_Shop.service.product.ProductService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SelectedProductService {
    SelectedProductRepository selectedProductRepository;
    OrdersRepository ordersRepository;
    CartItemRepository cartItemRepository;
    SelectedProductMapper selectedProductMapper;
    UserRepository userRepository;
    ProductService productService;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long valid_duration;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    public SelectedProductResponse createSelectedProduct(SelectedProductRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        SelectedProduct selectedProduct = selectedProductMapper.toSelectedProduct(request);
        ProductResponse productResponse = fetchProductResponse(selectedProduct.getProductId());
        selectedProduct.setUser(user);
        selectedProduct.setProductId(productResponse.getProductId());
        selectedProductRepository.save(selectedProduct);

        SelectedProductResponse selectedProductResponse =
                selectedProductMapper.toSelectedProductResponse(selectedProduct);
        selectedProductResponse.setProductId(productResponse);
        return selectedProductResponse;
    }

    public SelectedProductResponse updateSelectedProduct(String selectedId, UpdateSelectedProductRequest request) {
        SelectedProduct selectedProduct = selectedProductRepository
                .findById(selectedId)
                .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
        selectedProductMapper.updateSelectedProduct(selectedProduct, request);
        return selectedProductMapper.toSelectedProductResponse(selectedProductRepository.save(selectedProduct));
    }

    public void addSelectedProductWithUser(String orderId, AddSelectedProductRequest request) {
        var orders =
                ordersRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
        Set<SelectedProduct> selectedProducts = selectedProductRepository.findAllById(request.getSelectedId()).stream()
                .collect(Collectors.toSet());
        orders.getSelectedProducts().addAll(selectedProducts);
        ordersRepository.save(orders);
    }

    public void removeSelectedProductWithUser(String orderId, RemoveSelectedProductRequest request) {
        var orders =
                ordersRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
        Set<SelectedProduct> selectedProductSet =
                selectedProductRepository.findAllById(request.getSelectedId()).stream()
                        .collect(Collectors.toSet());
        orders.getSelectedProducts().removeAll(selectedProductSet);
        selectedProductRepository.deleteAll(selectedProductSet);
    }

    public void deleteAllSelectedProduct() {
        selectedProductRepository.deleteAll();
    }

    public SelectedProductResponse getSelectedProduct(String selectedId) {
        SelectedProduct selectedProduct = selectedProductRepository
                .findById(selectedId)
                .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
        userRepository.findById(selectedProduct.getUser().getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SelectedProductResponse selectedProductResponse =
                selectedProductMapper.toSelectedProductResponse(selectedProduct);
        ProductResponse productResponse = fetchProductResponse(selectedProduct.getProductId());
        selectedProductResponse.setProductId(productResponse);

        return selectedProductResponse;
    }

    public List<SelectedProductResponse> getAllSelectProduct() {
        return selectedProductRepository.findAll().stream()
                .map(selectedProduct -> {
                    SelectedProduct selectedProduct1 = selectedProductRepository
                            .findById(selectedProduct.getSelectedId())
                            .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
                    userRepository.findById(selectedProduct1.getUser().getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    SelectedProductResponse selectedProductResponse =
                            selectedProductMapper.toSelectedProductResponse(selectedProduct);
                    ProductResponse productResponse = fetchProductResponse(selectedProduct.getProductId());
                    selectedProductResponse.setProductId(productResponse);

                    return selectedProductResponse;
                })
                .collect(Collectors.toList());
    }

    public void deleteSelectedProduct(String selectedId) {
        selectedProductRepository.deleteById(selectedId);
    }

    public ProductResponse fetchProductResponse(String productId) {
        ProductResponse apiResponse = productService.getProduct(productId);
        return apiResponse;
    }
}
