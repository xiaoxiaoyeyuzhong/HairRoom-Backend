package com.fdt.management.model.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class UserUploadAvatarRequest implements Serializable {

    private MultipartFile userAvatar;

}
