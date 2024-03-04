package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.exception.InvalidLoginException;
import com.example.demo.model.SystemUser;
import com.example.demo.service.AuthService;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try{
        SystemUser user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);

        LoginResponse response = new LoginResponse(loginRequest.getUsername(),loginRequest.getPassword());
        response.setAccount(user.getAccount());
        response.setName(user.getName());

        return ResponseEntity.ok().headers(headers).body(response);
        }catch (InvalidLoginException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
        }
    }
}
