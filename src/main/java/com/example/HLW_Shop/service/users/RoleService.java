package com.example.HLW_Shop.service.users;

import com.example.HLW_Shop.exception.AppException;
import com.example.HLW_Shop.exception.ErrorCode;
import com.example.HLW_Shop.mapper.user.RoleMapper;
import com.example.HLW_Shop.model.dto.role.RoleRequest;
import com.example.HLW_Shop.model.dto.role.RoleResponse;
import com.example.HLW_Shop.repository.postgres.PermissionRepository;
import com.example.HLW_Shop.repository.postgres.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        log.info("Service create role");
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse getRole(String role) {
        return roleMapper.toRoleResponse(
                roleRepository.findById(role).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
