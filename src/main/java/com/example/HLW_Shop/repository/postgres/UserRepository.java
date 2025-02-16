package com.example.HLW_Shop.repository.postgres;

import com.example.HLW_Shop.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    //    Optional<UserInformationBasicResponse> findByUserInfomation(List<String> userIds);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);}
