package com.wechuang.mallshop.common.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IJPayUtil {
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator<String> iter = requestParams.keySet().iterator();

        while (iter.hasNext()) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        return params;
    }


    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;

        try {
            StringBuilder result = new StringBuilder();

            String line;
            for (br = request.getReader(); (line = br.readLine()) != null; result.append(line)) {
                if (result.length() > 0) {
                    result.append("\n");
                }
            }

            line = result.toString();
            return line;
        } catch (IOException var12) {
            IOException e = var12;
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var11) {
                    IOException e = var11;
                    e.printStackTrace();
                }
            }

        }
    }

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }
}
