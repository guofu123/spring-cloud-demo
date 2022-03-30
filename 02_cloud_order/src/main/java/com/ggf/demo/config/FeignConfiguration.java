package com.ggf.demo.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置feign请求的日志打印
 */
@Configuration
public class FeignConfiguration {
    /* 设置打印的类型 */
    @Bean
    Logger.Level feignLoggerLevel(){
        /**
         * ● NONE，无日志记录（默认）。
         * ● BASIC, 只记录请求方法和 URL 以及响应状态码和执行时间。
         * ● HEADERS, 记录基本信息以及请求和响应标头。
         * ● FULL, 记录请求和响应的标头、正文和元数据
         */
        return Logger.Level.FULL;
    }
}
