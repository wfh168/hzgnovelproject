package com.swxy.novel.domain.po;

import java.util.HashMap;
import java.util.Map;

public class NovelsStart {
    private  String SearchUrl;
    private String Accept;
    private String AcceptEncoding;
    private String AcceptLanguage;
    private String CacheControl;
    private String Connection;
    private String UserAgent;
    private Map<String,String> Cookies;
    private String saveDir;

    public String getSaveDir() {
        return "imgfile/";
    }

    public Map<String, String> getCookies() {
        // Cookies 字符串
        String cookieStr = "Hm_lvt_985c57aa6304c183e46daae6878b243b=1735279209; HMACCOUNT=2C07C3A61ED49149; getsite=bq02.cc; hm=0580c11e2ccece9deedc1d5f9ab9ada2; hmt=1735279212; Hm_lpvt_985c57aa6304c183e46daae6878b243b=1735279264";

        // 创建一个 Map 来存储 cookies
        Map<String, String> cookies = new HashMap<>();

        // 分割 cookies 字符串并放入 Map 中
        String[] cookiePairs = cookieStr.split("; ");
        for (String cookiePair : cookiePairs) {
            String[] keyValue = cookiePair.split("=");
            if (keyValue.length == 2) {
                cookies.put(keyValue[0], keyValue[1]);
            }
        }
        return cookies;
    }

    public String getAccept() {
        return "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7";
    }

    public String getAcceptEncoding() {
        return "gzip, deflate, br, zstd";
    }

    public String getAcceptLanguage() {
        return "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6";
    }

    public String getCacheControl() {
        return "no-cache";
    }


    public String getUserAgent() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36";
    }

    public String getSearchUrl() {
        return "https://www.bigee.cc/user/search.html?q=";
    }
}
