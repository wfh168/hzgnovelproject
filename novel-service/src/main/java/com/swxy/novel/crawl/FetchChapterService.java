package com.swxy.novel.crawl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swxy.novel.entity.po.Chapter;
import com.swxy.novel.mapper.ChapterMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class FetchChapterService {
    @Autowired
    private ChapterMapper chapterMapper;

    private static final Logger logger = LoggerFactory.getLogger(FetchChapterService.class);

    // Method to check if a chapter already exists in the database
    public boolean chapterExists(Long novelId, Long chapterId) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("novel_id", novelId).eq("chapter_id", chapterId);
        return chapterMapper.selectCount(queryWrapper) > 0;
    }

    // Method to fetch chapters concurrently
    public List<Chapter> fetchChapters(Document novelPage) {
        List<Chapter> chapters = new CopyOnWriteArrayList<>();
        List<String> failedUrls = new CopyOnWriteArrayList<>();
        Elements chapterLinks = novelPage.select("#newlist a");
        int totalChapters = chapterLinks.size();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Void>> tasks = new ArrayList<>();

        logger.info("总章节数: {}", totalChapters);

        // Submit tasks for fetching chapters
        for (int i = 0; i < totalChapters; i++) {
            Element chapterLink = chapterLinks.get(i);
            String chapterUrl = chapterLink.absUrl("href");
            String chapterName = chapterLink.text();

            int finalI = i;
            tasks.add(() -> {
                try {
                    Chapter chapter = new Chapter();
                    chapter.setTitle(chapterName);
                    chapter.setChapterId((long) (finalI + 1));  // Set chapter order
                    chapter.fetchContent(chapterUrl);  // Fetch content

                    // Check if the content was successfully fetched
                    if (chapter.getContent() != null && !chapter.getContent().isEmpty()) {
                        logger.info("成功获取章节: {}", chapterName);
                        chapters.add(chapter);
                    } else {
                        logger.warn("章节 '{}' 内容为空或未获取成功", chapterName);
                        failedUrls.add(chapterUrl);
                    }
                } catch (Exception e) {
                    logger.error("获取章节 '{}' 内容时出错: {}", chapterName, e.getMessage());
                    failedUrls.add(chapterUrl);
                }
                return null;
            });
        }

        // Execute all tasks
        try {
            executor.invokeAll(tasks);
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
                logger.error("线程池在指定时间内没有完成所有任务");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("线程池被中断: {}", e.getMessage());
            executor.shutdownNow();
        }

        // Retry failed URLs if any
        if (!failedUrls.isEmpty()) {
            logger.info("重新尝试获取失败的章节内容...");
            for (String failedUrl : failedUrls) {
                try {
                    Chapter retryChapter = new Chapter();
                    retryChapter.fetchContent(failedUrl); // Retry fetching the chapter content
                    if (retryChapter.getContent() != null && !retryChapter.getContent().isEmpty()) {
                        chapters.add(retryChapter);
                        logger.info("重新获取成功: {}", failedUrl);
                    } else {
                        logger.warn("重新获取章节 '{}' 内容仍然为空", failedUrl);
                    }
                } catch (Exception e) {
                    logger.error("重新获取章节 '{}' 时失败: {}", failedUrl, e.getMessage());
                }
            }
        }

        logger.info("总共获取到 {} 个章节", chapters.size());
        return chapters;
    }
}
