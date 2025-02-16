package com.example.HLW_Shop.service.orders;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.order.OrdersMapper;
import com.example.HLW_Shop.mapper.order.SelectedProductMapper;
import com.example.HLW_Shop.model.dto.order.*;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.entity.order.Orders;
import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import com.example.HLW_Shop.model.entity.user.User;
import com.example.HLW_Shop.repository.postgres.OrdersRepository;
import com.example.HLW_Shop.repository.postgres.SelectedProductRepository;
import com.example.HLW_Shop.repository.postgres.UserRepository;
import com.example.HLW_Shop.service.product.ProductService;
import com.example.HLW_Shop.service.users.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersService {
    OrdersRepository ordersRepository;
    OrdersMapper ordersMapper;
    UserRepository userRepository;
    SelectedProductRepository selectedProductRepository;
    SelectedProductMapper selectedProductMapper;
    ProductService productService;
    UserService userService;
//    KafkaTemplate<String, Object> kafkaTemplate;

//    BookClient bookClient;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long valid_duration;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;
    public Orders createOrders(CreateOrdersRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var order = ordersMapper.toOrders(request);
        order.setUser(user);
        final double[] amoutTotal = {0};
        Set<SelectedProduct> selectedProductSet =
                selectedProductRepository.findAllById(request.getSelectedProducts()).stream()
                        .map(selectedProduct -> {
                            SelectedProduct selectedProduct1 = selectedProductRepository
                                    .findById(selectedProduct.getSelectedId())
                                    .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
                            ProductResponse productResponse = productService.getProduct(selectedProduct1.getProductId());
                            amoutTotal[0] += selectedProduct1.getQuantity() * productResponse.getPrice();

                            return selectedProduct1;
                        })
                        .collect(Collectors.toSet());

        order.setVnpAmount(BigDecimal.valueOf(amoutTotal[0]));
        order.setSelectedProducts(selectedProductSet);
        ordersRepository.save(order);
        return ordersRepository.save(order);
    }

//    public Orders updateVNPayResponse(String orderId, VNPayResponseDTO responseDTO) {
//        var order =
//                ordersRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
//
//        order.setVnpTxnRef(orderId);
//        order.setVnpOrderInfo(responseDTO.getOrderInfo());
//        order.setVnpResponseCode(responseDTO.getResponseCode());
//        order.setVnpTransactionNo(responseDTO.getTransactionNo());
//        order.setVnpPayDate(responseDTO.getPayDate());
//        order.setVnpTransactionStatus(responseDTO.getTransactionStatus()); // update (19/07)
//
//        if ("00".equals(responseDTO.getResponseCode()) && "00".equals(responseDTO.getTransactionStatus())) {
//            order.setVnpTransactionStatus("PAYMENT_SUCCESS");
//
//            UserResponse userResponse = userService.getMyInfo();
//            log.info("userResponse: {}", userResponse);
//            NotificationEvent notificationEvent = NotificationEvent.builder()
//                    .channel("EMAIL")
//                    .recipient(userResponse.getEmail())
//                    .subject("It your bill book in system to " + LocalDate.now())
//                    .body("Thank you for purchasing our products")
//                    .build();
//
//            kafkaTemplate.send("notification-delivery", notificationEvent);
//        } else {
//            order.setVnpTransactionStatus("PAYMENT_FAILED");
//        }
//
//        return ordersRepository.save(order);
//    }

    public OrdersResponse getOrders(String orderId) {
        var order =
                ordersRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
        var user = userRepository
                .findById(order.getUser().getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<SelectedProduct> selectedProducts = selectedProducts(order.getSelectedProducts());
        Set<SelectedProductResponse> selectedProductResponses = selectedProductResponses(selectedProducts, user);
        OrdersResponse ordersResponse1 = ordersMapper.toOrdersResponse(order);
        ordersResponse1.setSelectedProducts(selectedProductResponses);
        return ordersResponse1;
    }

    public Set<OrdersResponse> getAllOrders() {
        return ordersRepository.findAll().stream()
                .map(orders -> {
                    Orders orders1 = ordersRepository
                            .findById(orders.getOrderId())
                            .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
                    OrdersResponse ordersResponse = ordersMapper.toOrdersResponse(orders1);
                    var user = userRepository
                            .findById(orders.getUser().getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    Set<SelectedProductResponse> selectedProductResponses =
                            selectedProductResponses(orders1.getSelectedProducts(), user);
                    ordersResponse.setSelectedProducts(selectedProductResponses);
                    return ordersResponse;
                })
                .collect(Collectors.toSet());
    }

//    public Set<SelectedProduct> selectedProductSet(Set<String> selectedProducts) {
//        Set<SelectedProduct> selectedProductSet = selectedProductRepository.findAllById(selectedProducts).stream()
//                .map(selectedProduct -> {
//                    SelectedProduct selectedProduct1 = selectedProductRepository
//                            .findById(selectedProduct.getSelectedId())
//                            .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
//                    return selectedProduct1;
//                })
//                .collect(Collectors.toSet());
//        return selectedProductSet;
//    }
    ;

    public Set<SelectedProduct> selectedProducts(Set<SelectedProduct> selectedProductSet) {
        Set<SelectedProduct> selectedProducts = selectedProductSet.stream()
                .map(selectedProduct -> {
                    var user = userRepository
                            .findById(selectedProduct.getUser().getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    SelectedProduct selectedProduct1 = selectedProductRepository
                            .findById(selectedProduct.getSelectedId())
                            .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
                    selectedProduct1.setUser(user);
                    selectedProduct1 = selectedProductRepository.save(selectedProduct);
                    return selectedProduct1;
                })
                .collect(Collectors.toSet());
        return selectedProducts;
    }

    public Set<SelectedProductResponse> selectedProductResponses(Set<SelectedProduct> selectedProducts, User user) {
        Set<SelectedProductResponse> selectedProductResponses = selectedProducts.stream()
                .map(selectedProduct -> {
                    SelectedProductResponse selectedProductResponse =
                            selectedProductMapper.toSelectedProductResponse(selectedProduct);
                    ProductResponse productResponse = fetchProductResponse(selectedProductResponse.getProductId().getProductId());
                    selectedProductResponse.setProductId(productResponse);
                    return selectedProductResponse;
                })
                .collect(Collectors.toSet());
        return selectedProductResponses;
    }
    ;

    public OrdersResponse updateOrders(String orderId, UpdateOrdersRequest request) {
        userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var order =
                ordersRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
        ordersMapper.updateOrder(order, request);
        return ordersMapper.toOrdersResponse(ordersRepository.save(order));
    }

    public void addSelectedProductWithOrdersWithUser(
            String userId, AddSelectedProductWithOrdersWithUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Orders> orders =
                ordersRepository.findAllById(request.getOrderId()).stream().collect(Collectors.toSet());
        user.getOrders().addAll(orders);
        userRepository.save(user);
    }

    public void removeSelectedProductWithOrdersWithUser(
            String userId, RemoveSelectedProductWithOrdersWithUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Orders> orders =
                ordersRepository.findAllById(request.getOrderId()).stream().collect(Collectors.toSet());
        user.getOrders().removeAll(orders);
        ordersRepository.deleteAll(orders);
    }

    public void deleteOrders(String orderId) {
        ordersRepository.deleteById(orderId);
    }

    public ProductResponse fetchProductResponse(String productId) {
        ProductResponse apiResponse = productService.getProduct(productId);
        return apiResponse;
    }
}
