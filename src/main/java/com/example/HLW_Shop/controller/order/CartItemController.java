package com.example.HLW_Shop.controller.order;


import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.order.*;
import com.example.HLW_Shop.model.dto.user.UserResponse;
import com.example.HLW_Shop.service.orders.CartItemService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartItem")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemController {

    CartItemService cartItemService;

    @PostMapping("/registration")
    public ApiResponse<CartItemResponse> createCartItem(@RequestBody @Valid CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .code(1000)
                .result(cartItemService.createCart(request))
                .build();
    }

    @PostMapping("/addCart/{userId}")
    public ApiResponse<UserResponse> addCart(@PathVariable String userId, @RequestBody AddCartItemRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(cartItemService.addCartItemToUser(userId, request))
                .build();
    }

    @DeleteMapping("/removeCart/{userId}")
    public ApiResponse<UserResponse> removeCart(
            @PathVariable String userId, @RequestBody RemoveCartItemRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(cartItemService.removeCartItemToUser(userId, request))
                .build();
    }

    @GetMapping("/{cartItemId}")
    public ApiResponse<CartItemResponse> getCartItem(@PathVariable String cartItemId) {
        return ApiResponse.<CartItemResponse>builder()
                .code(1000)
                .result(cartItemService.getCartItem(cartItemId))
                .build();
    }

    @GetMapping("/getAllCartItem")
    public ApiResponse<List<CartItemResponse>> getAllCartItem() {
        return ApiResponse.<List<CartItemResponse>>builder()
                .code(1000)
                .result(cartItemService.getAllCartItem())
                .build();
    }

    @DeleteMapping("/{cartItemId}")
    public ApiResponse<CartItemResponse> deleteCartItem(@PathVariable String cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return ApiResponse.<CartItemResponse>builder()
                .code(1000)
                .message("Delete CartItem success")
                .build();
    }

    @PutMapping("/updateCartItem/{cartItemId}")
    public ApiResponse<CartItemResponse> updateCartItem(
            @PathVariable String cartItemId, @RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .code(1000)
                .result(cartItemService.updateCartItem(cartItemId, request))
                .build();
    }
}
