package com.example.HLW_Shop.service.users;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HLW_Shop.config.Cloud;
import com.example.HLW_Shop.config.FileUtils;
import com.example.HLW_Shop.constant.PredefinedRole;
import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.order.CartItemMapper;
import com.example.HLW_Shop.mapper.order.OrdersMapper;
import com.example.HLW_Shop.mapper.order.SelectedProductMapper;
import com.example.HLW_Shop.mapper.user.UserMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.order.CartItemResponse;
import com.example.HLW_Shop.model.dto.order.OrdersResponse;
import com.example.HLW_Shop.model.dto.order.SelectedProductResponse;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.dto.user.*;
import com.example.HLW_Shop.model.entity.order.CartItem;
import com.example.HLW_Shop.model.entity.order.Orders;
import com.example.HLW_Shop.model.entity.order.SelectedProduct;
import com.example.HLW_Shop.model.entity.user.Role;
import com.example.HLW_Shop.model.entity.user.User;
import com.example.HLW_Shop.repository.postgres.*;
import com.example.HLW_Shop.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepositories;
    RoleRepository roleRepository;
    CartItemRepository cartItemRepository;
    OrdersRepository ordersRepository;
    OrdersMapper ordersMapper;
    SelectedProductMapper selectedProductMapper;
    SelectedProductRepository selectedProductRepository;
    UserMapper userMapper;
    CartItemMapper cartItemMapper;
    PasswordEncoder passwordEncoder;

    ProductService productService;

    Cloud cloud;

    public UserResponse createUser(UserCreateRequest request) {
        var user = userMapper.toUser(request);

        if (userRepositories.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXITSTED);
        }

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        try {
            userRepositories.save(user);
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }

        return userMapper.toUserResponse(user);
    }

    public void createPassword(String userId, PasswordCreationRequest request) {
        User user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword())) throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepositories.save(user);
    }

    public void changePassword(PasswordChangeRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepositories.save(user);
    }

    public void verifyPassword(PasswordVerifyRequest request) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepositories
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }

    @PreAuthorize("hasAuthority('GET_DATA')")
    public UserResponse getUser(String userId) {
        User user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.getCartItem().size();
        //        user.getSelectedProducts().size();

        UserResponse userResponse = userMapper.toUserResponse(user);

        Set<CartItemResponse> cartItemResponses = selectedCartItemResponse(userResponse.getCartItem());

        Set<OrdersResponse> ordersResponses = selectedOrdersResponse(userResponse.getOrders());

        userResponse.setCartItem(cartItemResponses);
        userResponse.setOrders(ordersResponses);

        return userResponse;
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        Set<CartItemResponse> cartItemResponses = selectedCartItemResponse(userResponse.getCartItem());

        Set<OrdersResponse> ordersResponses = selectedOrdersResponse(userResponse.getOrders());

        userResponse.setCartItem(cartItemResponses);
        userResponse.setOrders(ordersResponses);

        return userResponse;
    }

    @PreAuthorize("hasAuthority('GET_DATA')")
    public PageResponse<UserResponse> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = userRepositories.findAll(pageable);

        var userList = pageData.getContent().stream()
                .map(user -> {
                    var users = userRepositories
                            .findById(user.getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    return userMapper.toUserResponse(users);
                })
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(userList)
                .build();
    }

    public List<UserInformationBasicResponse> getAllUserInformationBasic() {
        List<User> userInformationBasicResponses =
                userRepositories.findAll().stream().toList();
        List<UserInformationBasicResponse> informationBasicResponse =
                convertToUserInformationBasicResponses(userInformationBasicResponses);
        return informationBasicResponse;
    }

    public UserInformationBasicResponse getUserInformationBasic(String userId) {
        var user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return UserInformationBasicResponse.builder()
                .username(user.getUsername())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .userId(user.getUserId())
                .build();
    }

    public List<UserInformationBasicResponse> convertToUserInformationBasicResponses(List<User> entity) {
        return entity.stream().map(this::convertToUserInformationResponse).collect(Collectors.toList());
    }

    public UserInformationBasicResponse convertToUserInformationResponse(User entity) {
        return new UserInformationBasicResponse(
                entity.getUserId(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getImages());
    }

    public Set<CartItemResponse> selectedCartItemResponse(Set<CartItemResponse> cartItemResponses) {
        Set<CartItemResponse> cartItemResponses1 = cartItemResponses.stream()
                .map(cartItemResponse -> {
                    CartItem cartItem = cartItemRepository
                            .findById(cartItemResponse.getCartItemId())
                            .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_EXISTED));
                    CartItemResponse itemResponse = cartItemMapper.toCartItemResponse(cartItem);
                    ProductResponse productResponse =
                            productService.getProduct(itemResponse.getProductId().getProductId());
                    itemResponse.setProductId(productResponse);
                    return itemResponse;
                })
                .collect(Collectors.toSet());
        return cartItemResponses1;
    }

    public Set<OrdersResponse> selectedOrdersResponse(Set<OrdersResponse> ordersResponse) {
        Set<OrdersResponse> ordersResponses = ordersResponse.stream()
                .map(ordersResponses1 -> {
                    Orders orders = ordersRepository
                            .findById(ordersResponses1.getOrderId())
                            .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
                    OrdersResponse ordersResponse1 = ordersMapper.toOrdersResponse(orders);
                    Set<SelectedProductResponse> selectedProductResponses = orders.getSelectedProducts().stream()
                            .map(selectedProductResponse -> {
                                SelectedProduct selectedProduct = selectedProductRepository
                                        .findById(selectedProductResponse.getSelectedId())
                                        .orElseThrow(() -> new AppException(ErrorCode.SELECTED_PRODUCT_NOT_EXISTED));
                                SelectedProductResponse selectedProductResponse1 =
                                        selectedProductMapper.toSelectedProductResponse(selectedProduct);
                                ProductResponse productResponse = productService.getProduct(selectedProduct.getProductId());
                                selectedProductResponse1.setProductId(productResponse);
                                return selectedProductResponse1;
                            })
                            .collect(Collectors.toSet());
                    ordersResponse1.setSelectedProducts(selectedProductResponses);
                    return ordersResponse1;
                })
                .collect(Collectors.toSet());
        return ordersResponses;
    }

    public UserResponse updateInformationUser(UpdateInformationRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateInformationUser(user, request);
        return userMapper.toUserResponse(userRepositories.save(user));
    }

    public Map<String, Object> uploadPhoto(MultipartFile file) throws IOException {
        String fileName = FileUtils.generateFileName("File", FileUtils.getExtension(file.getName()));
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);

        try {
            if (FileUtils.validateFile(file)) {
                file.transferTo(tempFile);

                Map<String, Object> uploadResult = cloud.cloudinaryConfig().uploader().upload(tempFile, ObjectUtils.emptyMap());
                return uploadResult;
            } else {
                throw new AppException(ErrorCode.UPLOAD_FILE_FAIL);
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAIL);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public void uploadImageUserCover(String userId, MultipartFile file) throws IOException {
        var user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Map<String, Object> uploadResult = uploadPhoto(file);
        String imageUrl = (String) uploadResult.get("url");

        user.setImages(imageUrl);
        userRepositories.save(user);
    }

    public void removeImageUser(String userId) {
        var user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXISTED));

            try {
                String publicId = extractPublicIdFromUrl(user.getImages());
                log.info("publicId: {}", publicId);

                Map result = cloud.cloudinaryConfig().uploader().destroy(publicId, ObjectUtils.emptyMap());

                if ("ok".equals(result.get("result"))) {
                    log.info("Successfully delete image from Cloudinary: {}", publicId);
                    user.setImages("");
                } else {
                    log.error("Failed to delete image from Cloudinary: {}", publicId);
                    throw new AppException(ErrorCode.REMOVE_FILE_FAIL);
                }
            } catch (IOException e) {
                throw new AppException(ErrorCode.REMOVE_FILE_FAIL);
            }
        userRepositories.save(user);
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        String[] urlParts = imageUrl.split("/");
        int uploadIndex = -1;
        for (int i = 0; i < urlParts.length; i++) {
            if ("upload".equals(urlParts[i])) {
                uploadIndex = i;
                break;
            }
        }

        if (uploadIndex == -1 || uploadIndex == urlParts.length - 1) {
            throw new IllegalArgumentException("Invalid Cloudinary URL format");
        }

        return String.join(
                        "/",
                        Arrays.stream(urlParts, uploadIndex + 1, urlParts.length)
                                .filter(part -> !part.startsWith("v"))
                                .collect(Collectors.toList()))
                .replaceFirst("[.][^.]+$", "");
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.info("Service: updateUser");
        User user = userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_EXITSTED));

        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepositories.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepositories.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUser() {
        userRepositories.deleteAll();
    }

    public void userExisted(String userId) {
        userRepositories.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
