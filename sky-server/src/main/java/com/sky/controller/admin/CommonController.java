package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "common")
public class CommonController {

    private final AliOssUtil aliOssUtil;

    public CommonController(AliOssUtil aliOssUtil, DishService dishService) {
        this.aliOssUtil = aliOssUtil;
    }

    @ApiOperation("upload")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("upload {}", file.getOriginalFilename());
        String URL = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
        return Result.success(URL);
    }
}
