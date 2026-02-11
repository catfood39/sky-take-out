package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<ShoppingCart> query(ShoppingCart cart);

    void insert(ShoppingCart cart);

    void update(ShoppingCart cart);
}
