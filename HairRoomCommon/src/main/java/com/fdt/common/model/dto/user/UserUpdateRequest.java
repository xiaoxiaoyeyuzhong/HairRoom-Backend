package com.fdt.common.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author fdt
 */
@Data
public class UserUpdateRequest implements Serializable {


    @TableField(exist = false)
    private static final long serialVersionUID = 285278132912197793L;
    /**
     * id
     */
    private Long id;

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