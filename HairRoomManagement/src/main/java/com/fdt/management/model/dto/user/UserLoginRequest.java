package com.fdt.management.model.dto.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author fdt
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -8950738174492371577L;

    private String userAccount;

    private String userPassword;
}
