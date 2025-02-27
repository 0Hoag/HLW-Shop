package com.example.HLW_Shop.controller.user;


import com.example.HLW_Shop.model.dto.ApiResponse;
import com.example.HLW_Shop.model.dto.role.RoleRequest;
import com.example.HLW_Shop.model.dto.role.RoleResponse;
import com.example.HLW_Shop.service.users.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .result(roleService.create(request))
                .build();
    }

    @GetMapping("/{role}")
    ApiResponse<RoleResponse> getRole(@PathVariable String role) {
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .result(roleService.getRole(role))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("role has been delete")
                .build();
    }
}
