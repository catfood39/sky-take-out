package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface CartService {
    void add(ShoppingCartDTO dto);

    List<ShoppingCart> list();

    void sub(ShoppingCartDTO dto);

    void clean();
}
