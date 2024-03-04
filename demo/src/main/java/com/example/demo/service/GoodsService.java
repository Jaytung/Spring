package com.example.demo.service;

import com.example.demo.dto.GoodsRequest;
import com.example.demo.dto.GoodsResponse;
import com.example.demo.model.Goods;
import com.example.demo.model.SystemUser;
import com.example.demo.repository.GoodsRepository;
import com.example.demo.repository.SystemUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SystemUserRepository userRepository;

    public GoodsResponse addGoods(GoodsRequest goodsRequest, String userId) {
        String goodsName = goodsRequest.getGoodsName();

        Goods newGoods = new Goods();
        newGoods.setName(goodsName);

        SystemUser user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("找不到該使用者: " + userId));

        newGoods.setCreateUser(user);
        newGoods.setUpdateUser(user);

        Goods savedGoods = goodsRepository.save(newGoods);

        GoodsResponse response = new GoodsResponse();
        response.setId(savedGoods.getId());
        response.setGoodsName(savedGoods.getName());

        return response;
    }

    public List<Goods> getAllGoods() {
        return goodsRepository.findAll();
    }

    public Goods getGoodsById(UUID id) {
        return goodsRepository.findById(id).orElse(null);
    }

    public Goods updateGoods(UUID id, String goodsName,String userId) {
        SystemUser user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("找不到該使用者: " + userId));

        return goodsRepository.findById(id).map(goods -> {
            goods.setUpdateUser(user);
            goods.setName(goodsName);
            return goodsRepository.save(goods);
        }).orElse(null);
    }

    public boolean deleteGoods(UUID id) {
        return goodsRepository.findById(id)
                .map(goods -> {
                    goodsRepository.delete(goods);
                    return true;
                }).orElse(false);
    }

}
