package com.swxy.novel.crawl;

import com.swxy.novel.entity.po.NovelsStart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class SearchService {
    public Map<Integer, String> search(String novelName) throws IOException {
        NovelsStart novelsStart = new NovelsStart();
        Document searchPage = Jsoup.connect(novelsStart.getSearchUrl())
                .header("User-Agent", novelsStart.getUserAgent())
                .data("searchkey", novelName)
                .post();

        Map<Integer, String> novelMap = new HashMap<>();
        Elements novelLinks = searchPage.select("h4 > a");
        int index = 1;
        for (Element link : novelLinks) {
            novelMap.put(index++, link.text() + ": " + link.absUrl("href"));
        }
        if (novelMap.isEmpty()) {
            System.out.println("未找到相关小说。");
        }
        return novelMap;
    }
}
