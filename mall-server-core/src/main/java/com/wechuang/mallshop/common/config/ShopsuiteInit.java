package com.wechuang.mallshop.common.config;

import com.wechuang.mallshop.sys.service.ConfigBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ShopsuiteInit implements CommandLineRunner {
    // [healthmall-ext] 原 Logger 名引用 core.config.CoreStartInit，该授权初始化类已随 core 自研替换移除
    private static final Logger log = LoggerFactory.getLogger(ShopsuiteInit.class);

    @Autowired
    ConfigBaseService configBaseService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        log.info("ShopsuiteInit...");
        configBaseService.init();
    }
}
