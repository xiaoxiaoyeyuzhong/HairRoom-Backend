package com.fdt.management.service.impl;

import com.aliyun.oss.OSS;
import com.fdt.management.config.OssConfig;
import com.fdt.management.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Resource
    private OSS ossClient;
    @Resource
    private OssConfig ossConfig;

    /**
     * 上传文件到指定目录
     * @param file     文件
     * @param dirPath  目标目录（如 "images/"）
     * @return 文件URL
     */
    public String uploadFileToDir(MultipartFile file, String dirPath) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String fullPath = buildFullPath(dirPath, fileName); // 拼接完整路径
        ossClient.putObject(ossConfig.getBucketName(), fullPath, file.getInputStream());
        return generateFileUrl(fullPath);
    }

    // 构建完整路径（处理目录格式）
    private String buildFullPath(String dirPath, String fileName) {
        if (dirPath == null || dirPath.isEmpty()) {
            return fileName; // 无目录直接返回文件名
        }
        // 确保目录以 "/" 结尾，并拼接文件名
        String normalizedDir = dirPath.endsWith("/") ? dirPath : dirPath + "/";
        return normalizedDir + fileName;
    }

    // 直接上传文件到根目录
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFileToDir(file, "");
    }

    // 生成唯一文件名（避免重复）
    private String generateUniqueFileName(String originalName) {
        return UUID.randomUUID() + "-" + originalName;
    }

    // 生成文件访问URL
    private String generateFileUrl(String fileName) {
        return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + fileName;
    }

    // todo 其他方法：deleteFile, downloadFile...
}

