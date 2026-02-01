package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException e){
        log.error("Message: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 捕获数据库操作异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e){
        // Duplicate entry 'tvergryipk' for key 'employee.idx_username'
        String message = e.getMessage();
        log.info(message);
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            return Result.error(split[2] + MessageConstant.ALREADY_EXISIT);
        }
        else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
