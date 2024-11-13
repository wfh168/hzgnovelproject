package com.swxy.chapter.domain.po;

public class NovelsStart {
    private  String SearchUrl;
    private String Accept;
    private String AcceptEncoding;
    private String AcceptLanguage;
    private String CacheControl;
    private String Connection;
    private String UserAgent;
    private String Cookie;
    private String saveDir;

    public String getSaveDir() {
        return "imgfile/";
    }

    public String getCookie() {
        return "Hm_lvt_0f20b8c5b0d2f108de80ac8128e0c587=1730009494; HMACCOUNT=2C07C3A61ED49149; hm=90f6f80988731c56a90452d2e7644539; Hm_lpvt_0f20b8c5b0d2f108de80ac8128e0c587=1730019880";
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
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0";
    }

    public String getSearchUrl() {
        return "https://www.biqgg.com/search/";
    }


}
