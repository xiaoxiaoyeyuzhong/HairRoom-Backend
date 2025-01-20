package com.fdt.common.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author fdt
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8621996257258624732L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
