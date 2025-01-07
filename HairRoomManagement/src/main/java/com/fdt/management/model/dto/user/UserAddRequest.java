package com.fdt.management.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author fdt
 */
@Data
public class UserAddRequest implements Serializable {
    
    private static final long serialVersionUID = 8243025191826477207L;
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色 ：Customer/ Staff / Manager
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;
    
}