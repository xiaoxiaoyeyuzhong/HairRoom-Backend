package com.fdt.portal.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
// 读取yml文件的支付宝支付配置
@ConfigurationProperties(prefix = "alipay")
@Log4j2
public class AliPayConfig {

    // 应用id
    private String appId;

    // 应用私钥
    private String appPrivateKey;

    // 支付宝公钥
    private String alipayPublicKey;

    // 支付宝异步通知回调地址
    private String notifyUrl;

    // 支付宝支付成功后的回调地址
    private String returnUrl;

    @PostConstruct
    public void init(){
        // 设置全局参数
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.appPrivateKey;
        config.alipayPublicKey = this.alipayPublicKey;
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
        log.info("支付宝支付配置初始化成功");
    }
}
