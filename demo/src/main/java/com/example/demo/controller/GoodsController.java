package com.example.demo.controller;

import com.example.demo.dto.GoodsRequest;
import com.example.demo.dto.GoodsResponse;
import com.example.demo.model.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private JwtService jwtService;

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉Bearer前缀
            return token;
        }
        return null;
    }

    private boolean validToken(String token) {
        if (jwtService.isValidToken(token)) {
            return true;
        }
        return false;
    }


    @PostMapping("/add")
    public ResponseEntity<GoodsResponse> addGoods(@RequestBody GoodsRequest goodsRequest, HttpServletRequest request) {

        String token = getToken(request);
        if (!validToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String userId = jwtService.getUserIdFromToken(token);

        GoodsResponse response = goodsService.addGoods(goodsRequest, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllGoods(HttpServletRequest request) {

        String token = getToken(request);
        if (!validToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("權限不足");
        }

        try {
            List<Goods> goodsList = goodsService.getAllGoods();
            List<GoodsResponse> responseList = goodsList.stream()
                    .map(goods -> new GoodsResponse(goods.getId(), goods.getName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGoodsById(@PathVariable UUID id, HttpServletRequest request) {

        String token = getToken(request);
        if (!validToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("權限不足");
        }

        try {
            Goods goods = goodsService.getGoodsById(id);
            if (goods != null) {
                return ResponseEntity.ok(new GoodsResponse(goods.getId(), goods.getName()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoods(@PathVariable UUID id, @RequestBody GoodsRequest goodReq, HttpServletRequest request) {
        String token = getToken(request);
        if (!validToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("權限不足");
        }
        try {
            String userId = jwtService.getUserIdFromToken(token);
            Goods updatedGoods = goodsService.updateGoods(id, goodReq.getGoodsName(),userId);
            if (updatedGoods != null) {
                GoodsResponse response = new GoodsResponse(updatedGoods.getId(), updatedGoods.getName());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoods(@PathVariable UUID id, HttpServletRequest request) {
        String token = getToken(request);
        if (!validToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("權限不足");
        }
        try {
            boolean deleted = goodsService.deleteGoods(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
        }
    }
}
