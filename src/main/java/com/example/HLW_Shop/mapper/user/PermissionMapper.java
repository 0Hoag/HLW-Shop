package com.example.HLW_Shop.mapper.user;


import com.example.HLW_Shop.model.dto.role.PermissionRequest;
import com.example.HLW_Shop.model.dto.role.PermissionResponse;
import com.example.HLW_Shop.model.entity.user.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
