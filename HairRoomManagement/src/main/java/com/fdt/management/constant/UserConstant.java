package com.fdt.management.constant;

/**
 * 用户常量
 *
 * @author fdt
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 系统用户 id（虚拟用户）
     */
    long SYSTEM_USER_ID = 0;

    //  region 权限

    /**
     * 默认权限
     */
    String DEFAULT_ROLE = "Customer";

    /**
     * 员工权限
     */
    String STAFF_ROLE = "staff";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "Manager";

    // endregion
}
