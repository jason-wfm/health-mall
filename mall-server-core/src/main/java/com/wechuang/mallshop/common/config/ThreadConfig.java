package com.wechuang.mallshop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadConfig {

    // [healthmall-ext] nacos starter 会注册同类型 executor bean，标注 @Primary 消除 @Resource 按类型注入歧义
    @Primary
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(20, 200,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

}
