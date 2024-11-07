package com.swxy.novel.crawl;
import com.swxy.novel.entity.po.Novel;
import com.swxy.novel.entity.po.NovelsStart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NovelDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(NovelDetailsService.class);

    public Novel fetchDetails(String novelUrl, String saveDir) throws IOException {
        NovelsStart novelsStart = new NovelsStart();
        Document novelPage = Jsoup.connect(novelUrl)
                .header("User-Agent", novelsStart.getUserAgent())
                .get();

        Novel novel = new Novel();
        novel.setNovelDetails(novelPage);
        // 下载封面图片并设置路径
        String imagePath = ImageDownloader.downloadImage(novelPage, saveDir);
        if (imagePath != null) {
            novel.setImagePath(imagePath);
        }

        logger.info(novel.toString());

        // 处理章节等其他信息
        ImageDownloader.downloadImage(novelPage, novelsStart.getSaveDir());
        return novel;
    }
}

