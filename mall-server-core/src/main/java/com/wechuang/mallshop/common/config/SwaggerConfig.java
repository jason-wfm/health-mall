package com.wechuang.mallshop.common.config;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * OpenAPI 配置 (Spring Boot 3)
 *
 * @author Xinze
 * @since 2018-02-22 11:29:05
 */
@OpenAPIDefinition
@Configuration
public class SwaggerConfig {
    @Resource
    private ConfigProperties config;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(apiInfo())
                .security(securityRequirements())
                .components(securityComponents());

        // 配置自定义host
        if (StrUtil.isNotBlank(config.getSwaggerHost())) {
            openAPI.addServersItem(new Server().url(config.getSwaggerHost()));
        }

        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title(config.getSwaggerTitle())
                .description(config.getSwaggerDescription())
                .version(config.getSwaggerVersion())
                .termsOfService("");
    }

    private List<SecurityRequirement> securityRequirements() {
        return Collections.singletonList(
                new SecurityRequirement().addList("Authorization")
        );
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .description("JWT认证"));
    }
}