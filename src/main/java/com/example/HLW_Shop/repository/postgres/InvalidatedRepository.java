package com.example.HLW_Shop.repository.postgres;

import com.example.HLW_Shop.model.entity.user.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {}
