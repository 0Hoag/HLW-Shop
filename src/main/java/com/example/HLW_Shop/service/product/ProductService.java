package com.example.HLW_Shop.service.product;

import com.cloudinary.utils.ObjectUtils;
import com.example.HLW_Shop.config.Cloud;
import com.example.HLW_Shop.config.FileUtils;
import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.product.ProductMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.request.CreateProductRequest;
import com.example.HLW_Shop.model.dto.product.response.ProductResponse;
import com.example.HLW_Shop.model.dto.product.request.ProductUpdateRequest;
import com.example.HLW_Shop.model.entity.product.Product;
import com.example.HLW_Shop.repository.mongo.product.AuthorRepository;
import com.example.HLW_Shop.repository.mongo.product.CategoryRepository;
import com.example.HLW_Shop.repository.mongo.product.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    Cloud cloud;

    public ProductResponse toCreateProduct(CreateProductRequest request) {
        var author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));

        request.getCategoriesId().stream().map(categoriesId ->
                categoryRepository.findById(categoriesId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));

        Product product = productMapper.toProduct(request);
        product.setAuthorId(author.getAuthorId());
        productRepository.save(product);

        author.getProduct().add(product.getProductId());
        authorRepository.save(author);

        return productMapper.toProductResponse(product);
    }

    public ProductResponse getProduct(String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return productMapper.toProductResponse(product);
    }

    public PageResponse<ProductResponse> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> pageData = productRepository.findAll(pageable);

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(productMapper::toProductResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteProduct(String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        var author = authorRepository.findById(product.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
        author.getProduct().remove(productId);

        authorRepository.save(author);

        productRepository.deleteById(product.getProductId());
    }

    public Boolean updateProduct(String productId, ProductUpdateRequest request) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);

        return true;
    }

    public void uploadImageProduct(String productId, MultipartFile file) throws IOException {
        var product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        Map<String, Object> uploadResult = uploadPhoto(file);
        String imageUrl = (String) uploadResult.get("url");

        product.getImage().add(imageUrl);
        productRepository.save(product);
    }

    public Map<String, Object> uploadPhoto(MultipartFile file) throws IOException {
        String fileName = FileUtils.generateFileName("File", FileUtils.getExtension(file.getName()));
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        try {
            if (FileUtils.validateFile(file)) {
                file.transferTo(tempFile);

                Map<String, Object> uploadResult = cloud.cloudinaryConfig().uploader().upload(tempFile, ObjectUtils.emptyMap());
                return uploadResult;
            }else {
                throw new AppException(ErrorCode.UPLOAD_FILE_FAIL);
            }
        }catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAIL);
        }finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
