package com.swxy.novel.crawl;

import com.swxy.novel.domain.po.NovelsStart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class SearchService {
    public Map<Integer, String> search(String novelName) throws IOException, JSONException {
        NovelsStart novelsStart = new NovelsStart();
//        String gbkEncodedSearchKey=EncodeUtils.encodeToGBK(novelName);
        String encodedSearchKey = null;
        try {
            encodedSearchKey = java.net.URLEncoder.encode(novelName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String SearchUrl=novelsStart.getSearchUrl()+ encodedSearchKey;
        Document searchPage = Jsoup.connect(SearchUrl).cookies(novelsStart.getCookies())
                .header("User-Agent", novelsStart.getUserAgent())
                .get();

        String jsonResponse = searchPage.body().text();  // 获取网页中的 JSON 数据
        JSONArray jsonArray = new JSONArray(jsonResponse);  // 将 JSON 字符串转为 JSON 数组
        Map<Integer, String> novelMap = new HashMap<>();
        int index =1;
        if (jsonArray.length() == 0) {
            System.out.println("没有找到相关小说。");
        } else {
            System.out.println("搜索结果：");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject book = jsonArray.getJSONObject(i);  // 获取每个小说对象
                String name = book.getString("articlename");  // 获取小说名称
                String novelLink = book.getString("url_list");  // 获取小说链接

                // 将小说名称和链接存储到 Map 中，键为 "1", "2", "3" 等
                novelMap.put(index++,name + "https://www.bigee.cc" + novelLink);
            }
        }
        return novelMap;
    }
}
