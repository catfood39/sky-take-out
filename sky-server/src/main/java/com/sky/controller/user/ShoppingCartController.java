package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
