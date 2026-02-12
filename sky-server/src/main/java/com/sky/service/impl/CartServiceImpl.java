package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.CartMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    public CartServiceImpl(CartMapper cartMapper, DishMapper dishMapper, SetmealMapper setmealMapper) {
        this.cartMapper = cartMapper;
        this.dishMapper = dishMapper;
        this.setmealMapper = setmealMapper;
    }

    @Override
    public void add(ShoppingCartDTO dto) {
        if ((dto.getDishId() == null && dto.getSetmealId() == null)
        || (dto.getDishId() != null && dto.getSetmealId() != null)) {
            throw new ShoppingCartBusinessException("唯一性冲突");
        }
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(dto, cart);
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = cartMapper.query(cart);
        if (list == null || list.isEmpty()) {
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            if (dto.getDishId() != null) {
                Dish dish = dishMapper.getById(dto.getDishId());
                cart.setName(dish.getName());
                cart.setImage(dish.getImage());
                cart.setAmount(dish.getPrice());
            }
            else {
                Setmeal setmeal = setmealMapper.getById(dto.getSetmealId());
                cart.setName(setmeal.getName());
                cart.setImage(setmeal.getImage());
                cart.setAmount(setmeal.getPrice());
            }
            cartMapper.insert(cart);
        }
        else if (list.size() == 1) {
            cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            cartMapper.update(cart);
        }
        else {
            throw new ShoppingCartBusinessException("服务器内部数据库约束冲突");
        }
    }

    @Override
    public List<ShoppingCart> list() {
        return cartMapper.list(BaseContext.getCurrentId());
    }
}
