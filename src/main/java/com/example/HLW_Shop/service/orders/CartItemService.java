package com.example.HLW_Shop.service.orders;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.order.CartItemMapper;
import com.example.HLW_Shop.mapper.user.UserMapper;
import com.example.HLW_Shop.model.dto.order.*;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.dto.user.UserResponse;
import com.example.HLW_Shop.model.entity.order.CartItem;
import com.example.HLW_Shop.model.entity.user.User;
import com.example.HLW_Shop.repository.postgres.CartItemRepository;
import com.example.HLW_Shop.repository.postgres.UserRepository;
import com.example.HLW_Shop.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemService {
    UserRepository userRepository;
    ProductService productService;
    UserMapper userMapper;
    CartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;
    String NOTIFICATION_TOPIC = "cart-notifications";
//    NotificationClient notificationClient;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long valid_duration;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    public CartItemResponse createCart(CartItemRequest request) {
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        String token = generateToken(user);
        // create CartItem
        CartItem cartItem = cartItemMapper.toCartItem(request);
        cartItem.setUser(user);
        cartItem.setCartItemId(request.getProductId());
        cartItem = cartItemRepository.save(cartItem);

//        var book = fetchProductResponse(cartItem.getProductId());
//        CartNotificationRequest cartNotificationRequest = CartNotificationRequest.builder()
//                .bookId(book.getBookId())
//                .bookTitle(book.getBookTitle())
//                .bookImage(book.getImage())
//                .quantity(cartItem.getQuantity())
//                .price(cartItem.getQuantity() * book.getPrice())
//                .userId(user.getUserId())
//                .idRead(false)
//                .type(NotificationType.ADD_TO_CART)
//                .build();
//        createCartNotification(cartNotificationRequest, token);

        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        ProductResponse productResponse = fetchProductResponse(cartItem.getProductId());
        cartItemResponse.setProductId(productResponse);

        return cartItemResponse;
    }

    // replace 7/9
    public CartItemResponse getCartItem(String cartItemId) {
        var cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_EXISTED));
        userRepository.findById(cartItem.getUser().getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        ProductResponse productResponse = fetchProductResponse(cartItem.getProductId());
        cartItemResponse.setProductId(productResponse);

        return cartItemResponse;
    }

    public List<CartItemResponse> getAllCartItem() {
        return cartItemRepository.findAll().stream()
                .map(cartItem -> {
                    userRepository.findById(cartItem.getUser().getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
                    ProductResponse productResponse = fetchProductResponse(cartItem.getProductId());
                    cartItemResponse.setProductId(productResponse);
                    return cartItemResponse;
                })
                .toList();
    }

    public CartItemResponse updateCartItem(String cartItemId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_EXISTED));
        cartItemMapper.updateCartItem(cartItem, request);
        return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
    }

    public UserResponse addCartItemToUser(String userId, AddCartItemRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Set<CartItem> cartItems =
                cartItemRepository.findAllById(request.getCartItemId()).stream().collect(Collectors.toSet());
        user.getCartItem().addAll(cartItems);

        userRepository.save(user);

        Set<CartItemResponse> cartItemResponses = selectedCartItemResponse(cartItems); // update 08/02

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setCartItem(cartItemResponses);

        return userResponse;
    }

    public UserResponse removeCartItemToUser(String userId, RemoveCartItemRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<CartItem> cartItems =
                cartItemRepository.findAllById(request.getCartItemId()).stream().collect(Collectors.toSet());
        user.getCartItem().removeAll(cartItems);
        userRepository.save(user);

        Set<CartItemResponse> cartItemResponses = selectedCartItemResponse(cartItems); // update 08/02

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setCartItem(cartItemResponses);
        cartItemRepository.deleteAll(cartItems);

        return userResponse;
    }

    public Set<CartItemResponse> selectedCartItemResponse(Set<CartItem> cartItems) { // update 08/02
        Set<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(cartItem -> {
                    CartItemResponse response = cartItemMapper.toCartItemResponse(cartItem);
                    ProductResponse productResponse = fetchProductResponse(cartItem.getProductId());
                    response.setProductId(productResponse);
                    return response;
                })
                .collect(Collectors.toSet());
        return cartItemResponses;
    }

    public void deleteCartItem(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public ProductResponse fetchProductResponse(String productId) {
        ProductResponse apiResponse = productService.getProduct(productId);
        return apiResponse;
    }

//    public CartNotificationResponse createCartNotification(CartNotificationRequest request, String token) {
//        ApiResponse<CartNotificationResponse> cartNotificationResponseApiResponse =
//                notificationClient.createCartNotification(request, "Bearer " + token);
//        return cartNotificationResponseApiResponse.getResult();
//    }
}
