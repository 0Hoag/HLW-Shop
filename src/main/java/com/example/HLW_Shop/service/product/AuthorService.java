package com.example.HLW_Shop.service.product;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.product.AuthorMapper;
import com.example.HLW_Shop.mapper.product.ProductMapper;
import com.example.HLW_Shop.model.dto.PageResponse;
import com.example.HLW_Shop.model.dto.product.response.AuthorResponse;
import com.example.HLW_Shop.model.dto.product.request.CreateAuthorRequest;
import com.example.HLW_Shop.model.dto.product.request.UpdateAuthorRequest;
import com.example.HLW_Shop.model.entity.product.Author;
import com.example.HLW_Shop.repository.mongo.product.AuthorRepository;
import com.example.HLW_Shop.repository.mongo.product.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorService {
    AuthorRepository authorRepository;
    AuthorMapper authorMapper;
    ProductRepository productRepository;
    ProductMapper productMapper;

    public AuthorResponse toCreateAuthor(CreateAuthorRequest request) {
        var author = authorMapper.toAuthor(request);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    public AuthorResponse toGetAuthor(String authorId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
        return authorMapper.toAuthorResponse(author);
    }

    public PageResponse<AuthorResponse> toGetAllAuthor(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Author> pageData = authorRepository.findAll(pageable);

        return PageResponse.<AuthorResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(authorMapper::toAuthorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteAuthor(String authorId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
        authorRepository.deleteById(author.getAuthorId());
    }

    public boolean updateAuthor(String authorId, UpdateAuthorRequest request) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_EXISTED));

        authorMapper.updateAuthor(author, request);

        return true;
    }
}
