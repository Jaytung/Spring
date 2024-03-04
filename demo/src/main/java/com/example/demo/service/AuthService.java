package com.example.demo.service;

import com.example.demo.exception.InvalidLoginException;
import com.example.demo.model.SystemUser;
import com.example.demo.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private SystemUserRepository userRepository;

    public SystemUser authenticate(String account, String password) {
        return userRepository.findByAccountAndPassword(account, password)
                .orElseThrow(() -> new InvalidLoginException("帳號或密碼錯誤"));
    }
}
