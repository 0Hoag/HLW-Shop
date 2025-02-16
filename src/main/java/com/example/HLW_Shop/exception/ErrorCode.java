package com.example.HLW_Shop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "UNCATEGORIZE_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
    USER_EXITSTED(1002, "USER EXITED", HttpStatus.BAD_REQUEST),
    EMAIL_EXITSTED(1002, "EMAIL EXITED", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_EXISTED(1003, "SELECTED_PRODUCT_NOT_EXISTED", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "USERNAME MUST AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "PASSWORD NOT BE AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "USER_NOT_EXISTED", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZE(1008, "YOU DO NOT HAVE PREMISSION", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "YOU AGE MUST BE AT least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1010, "PASSWORD_EXISTED", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1011, "PERMISSION_NOT_FOUND", HttpStatus.BAD_REQUEST),
    CART_ITEM_EXISTED(1012, "CART_ITEM_EXISTED", HttpStatus.BAD_REQUEST),
    ORDERS_NOT_EXISTED(1013, "ORDERS_NOT_EXISTED", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_EXISTED(1014, "Image not existed", HttpStatus.BAD_REQUEST),

    UPLOAD_FILE_FAIL(1015, "Upload file to fail!", HttpStatus.BAD_REQUEST),
    REMOVE_FILE_FAIL(1016, "Remove file to fail!", HttpStatus.BAD_REQUEST),

    OUT_OF_STOCK_PRODUCT(1017, "Out of stock product", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1018, "Product not existed!", HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_EXISTED(1019, "Author not existed!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1020, "Category not existed!", HttpStatus.BAD_REQUEST),
    CATEGORY_DAD_NOT_EXISTED(1021, "Category dad not existed!", HttpStatus.BAD_REQUEST),
    USER_OR_PRODUCT_NOT_EXISTED(1022, "User or Product not existed!", HttpStatus.BAD_REQUEST),

    REVIEW_NOT_EXISTED(1023, "Review not existed!", HttpStatus.BAD_REQUEST),
    REVIEW_IT_EXISTED(1024, "User have rated this product!", HttpStatus.BAD_REQUEST),
    REPLY_NOT_EXISTED(1025, "Reply not existed!", HttpStatus.BAD_REQUEST),

    YOU_NEED_TO_LOGIN(7777, "You need to login in to do this!", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    ErrorCode() {}
}
