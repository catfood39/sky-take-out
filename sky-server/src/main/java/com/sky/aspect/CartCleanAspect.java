package com.sky.aspect;

import com.sky.entity.ShoppingCart;
import com.sky.mapper.CartMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Aspect
@Slf4j
public class CartCleanAspect {

    private final CartMapper cartMapper;

    public CartCleanAspect(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Pointcut("execution(* com.sky.mapper.*.*(..)) &&" +
            "@annotation(com.sky.annotation.CartClean)")
    public void cartCleanPointCut() {}

    // TODO: 删除操作因为是批量进行的，参数类型是List<Long> ids，无法适配
    @AfterReturning("cartCleanPointCut()")
    public void cartClean(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object entity = joinPoint.getArgs()[0];
        Class<?> clazz = entity.getClass();
        Long id = (Long) clazz.getDeclaredMethod("getId").invoke(entity);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> declaringType = signature.getDeclaringType();
        ShoppingCart cart = new ShoppingCart();
        if (declaringType == DishMapper.class) {
            cart.setDishId(id);
        }
        else if (declaringType == SetmealMapper.class) {
            cart.setSetmealId(id);
        }
        else {
            throw new RuntimeException("clean cart aspect error");
        }
        cartMapper.delete(cart);
    }

}
