package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/common/upload")
public class UploadController {

    // TODO 等阿里云正常了再写
    @PostMapping
    public Result<String> upload() {
        log.info("开始上传文件");
        return Result.success();
    }

}
