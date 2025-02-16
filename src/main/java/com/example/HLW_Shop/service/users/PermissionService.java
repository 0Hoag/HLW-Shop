package com.example.HLW_Shop.service.users;


import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.user.PermissionMapper;
import com.example.HLW_Shop.model.dto.role.PermissionRequest;
import com.example.HLW_Shop.model.dto.role.PermissionResponse;
import com.example.HLW_Shop.model.entity.user.Permission;
import com.example.HLW_Shop.repository.postgres.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public PermissionResponse getPermission(String permission) {
        return permissionMapper.toPermissionResponse(permissionRepository
                .findById(permission)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)));
    }

    public List<PermissionResponse> getALL() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
