package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐不存在口味，套餐id即可唯一确定一项购物车item
 * 菜品存在口味区别，不同口味的同一个菜品在购物的视角是两项不同的item
 */

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    private final CartService cartService;

    public ShoppingCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto) {

        cartService.add(dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {

        List<ShoppingCart> list =  cartService.list();
        return Result.success(list);
    }

    @PostMapping("/sub")
    public Result list(@RequestBody ShoppingCartDTO dto) {
        cartService.sub(dto);
        return Result.success();
    }

    @DeleteMapping("/clean")
    public Result clean() {
        cartService.clean();
        return Result.success();
    }

}
