package com.example.HLW_Shop.mapper.user;


import com.example.HLW_Shop.model.dto.role.RoleRequest;
import com.example.HLW_Shop.model.dto.role.RoleResponse;
import com.example.HLW_Shop.model.entity.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
