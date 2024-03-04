package com.example.demo.repository;

import com.example.demo.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SystemUserRepository extends JpaRepository<SystemUser, UUID> {
    Optional<SystemUser> findByAccountAndPassword(String account, String password);
}
