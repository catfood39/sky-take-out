package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<ShoppingCart> query(ShoppingCart cart);

    void insert(ShoppingCart cart);

    void update(ShoppingCart cart);

    List<ShoppingCart> list(Long userId);

    void delete(ShoppingCart cart);

    void sub(ShoppingCart dto);

    void deleteByUserId(Long currentId);
}
