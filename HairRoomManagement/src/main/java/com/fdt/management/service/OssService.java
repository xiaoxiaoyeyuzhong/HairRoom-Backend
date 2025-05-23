package com.fdt.management.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {
    String uploadFile(MultipartFile file) throws IOException;

    String uploadFileToDir(MultipartFile file, String dirPath) throws IOException;
}
