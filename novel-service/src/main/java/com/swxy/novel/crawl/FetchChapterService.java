package com.swxy.novel.crawl;
import com.swxy.novel.entity.Chapter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class ChapterService {
    private static final Logger logger = LoggerFactory.getLogger(ChapterService.class);

    public void fetchChapters(Document novelPage) {
        Elements chapterLinks = novelPage.select("#newlist a");
        ExecutorService executor = Executors.newFixedThreadPool(4); // 创建线程池

        for (Element chapterLink : chapterLinks) {
            String chapterUrl = chapterLink.absUrl("href");
            String chapterName = chapterLink.text();

            executor.submit(() -> {
                try {
                    Chapter chapter = new Chapter(chapterUrl);
                    chapter.fetchContent(); // 假设 fetchContent 方法会处理内容的获取
                    logger.info("章节: {}", chapterName);
                    logger.info("章节内容: {}", chapter.getContent());
                    System.out.println("章节: " + chapterName);
                    System.out.println("章节内容: " + chapter.getContent());
                    System.out.println("-------------------------------------------------");
                } catch (Exception e) {
                    logger.error("获取章节 '{}' 内容时出错: {}", chapterName, e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES); // 等待所有任务完成
        } catch (InterruptedException e) {
            logger.error("线程池被中断: {}", e.getMessage());
        }
    }
}
