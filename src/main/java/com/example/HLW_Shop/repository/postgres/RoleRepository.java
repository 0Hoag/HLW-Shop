package com.example.HLW_Shop.repository.postgres;

import com.example.HLW_Shop.model.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
