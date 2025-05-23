package com.fdt.management.controller;


import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.management.service.OssService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/oss")
public class OssController {
    @Resource
    private OssService ossService;

    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile file,
                                           @RequestPart(value = "dir", required = false) String dir) {
        try {
            String url = ossService.uploadFileToDir(file,dir);
            return ResultUtils.success(url);
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
    }
}
