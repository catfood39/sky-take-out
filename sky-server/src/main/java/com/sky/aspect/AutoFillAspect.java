package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) &&" +
            "@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始进行公共字段填充");

        // 获取连接点方法的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        OperationType type = annotation.value();

        // 获取连接点方法的第一个参数（约定第一个参数为数据库记录实体引用）
        Object entity = joinPoint.getArgs()[0];
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        // 通过反射为字段赋值
        if (type == OperationType.INSERT) {
            Class<?> clazz = entity.getClass();
            clazz.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class)
                    .invoke(entity, time);
            clazz.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class)
                    .invoke(entity, id);
            clazz.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class)
                    .invoke(entity, time);
            clazz.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class)
                    .invoke(entity, id);
        }
        else if (type == OperationType.UPDATE) {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            setUpdateTime.invoke(entity, time);
            setUpdateUser.invoke(entity, id);
        }

    }

}
