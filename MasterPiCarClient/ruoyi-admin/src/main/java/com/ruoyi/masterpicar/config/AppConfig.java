package com.ruoyi.masterpicar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 全局配置类
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate Bean
     * 项目中只需要注入一次，就可以在任何 Service 或 Controller 使用
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
