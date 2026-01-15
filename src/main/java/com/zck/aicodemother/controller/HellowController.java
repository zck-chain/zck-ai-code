package com.zck.aicodemother.controller;

import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵承康
 * @date 2026/1/15
 */
@RestController
@RequestMapping("/hello")
public class HellowController {
    @GetMapping("/world")
    public BaseResponse hello() {
        return ResultUtils.success("Hello World");
    }
}
