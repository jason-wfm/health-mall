package com.wechuang.mallshop.sys.controller.front;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/front/sys/proxy")
public class ProxyController {

    @GetMapping("/oss")
    public void proxyOssResource(@RequestParam String url, HttpServletResponse response) throws IOException {
        // 验证URL是否为合法的OSS资源URL
        if (!isValidOssUrl(url)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        try {
            // 从OSS获取资源
            URL ossUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) ossUrl.openConnection();
            connection.setRequestMethod("GET");

            // 设置响应头
            response.setContentType(connection.getContentType());
            response.setContentLength(connection.getContentLength());

            // 将OSS资源流式传输到客户端
            try (InputStream inputStream = connection.getInputStream();
                 OutputStream outputStream = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private boolean isValidOssUrl(String url) {
        // 实现URL验证逻辑
        return url != null && url.contains("aliyuncs.com");
    }
}