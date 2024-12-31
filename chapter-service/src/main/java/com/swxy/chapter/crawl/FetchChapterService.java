package com.swxy.chapter.crawl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.swxy.chapter.domain.dto.ChapterDTO;
import com.swxy.chapter.domain.po.Chapter;
import com.swxy.chapter.mapper.ChapterMapper;
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
    public List<ChapterDTO> fetchChapters(Document novelPage) {
        List<ChapterDTO> chapters = new CopyOnWriteArrayList<>();
        List<String> failedUrls = new CopyOnWriteArrayList<>();
        // 获取两个章节链接
        Elements chapterLinks = novelPage.select("body > div.listmain > dl > dd > a");
        Elements chapterLinks1 = novelPage.select("body > div.listmain > dl > span > dd > a");

        // 1. 删除 chapterLinks 中包含 javascript:dd_show() 的链接
        List<Element> validChapterLinks = new ArrayList<>();
        for (Element link : chapterLinks) {
            if (!link.attr("href").startsWith("javascript:dd_show()")) {
                validChapterLinks.add(link);  // 只保留有效链接
            }
        }

        // 2. 获取 chapterLinks 前10个元素
        List<Element> firstTenLinks = new ArrayList<>();
        int limit = Math.min(10, validChapterLinks.size());  // 处理少于10个的情况
        for (int i = 0; i < limit; i++) {
            firstTenLinks.add(validChapterLinks.get(i));
        }

        // 3. 获取 chapterLinks 后面的部分
        List<Element> remainingLinks = new ArrayList<>();
        for (int i = 10; i < validChapterLinks.size(); i++) {
            remainingLinks.add(validChapterLinks.get(i));
        }

        // 4. 将 chapterLinks1 的链接添加到最终列表
        List<Element> finalLinksList = new ArrayList<>();
        finalLinksList.addAll(firstTenLinks);  // 添加前10个有效链接
        finalLinksList.addAll(chapterLinks1);  // 添加 chapterLinks1 中的链接
        finalLinksList.addAll(remainingLinks);  // 添加剩余的有效链接到末尾

        // 将结果存储为 Elements 类型
        Elements finalLinks = new Elements(finalLinksList);
        System.out.println(finalLinks.size());
        int totalChapters = finalLinks.size();
        logger.info("总章节数: {}", totalChapters);
        if (totalChapters > 500) {
            totalChapters = 300;
            logger.info("为了节省空间总章节数大于500插入{}条", totalChapters);
        }
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Void>> tasks = new ArrayList<>();
        // Submit tasks for fetching chapters

        // Submit tasks for fetching chapters
        for (int i = 0; i < totalChapters; i++) {
            Element chapterLink = finalLinks.get(i);
            String chapterUrl = chapterLink.absUrl("href");
            String chapterName = chapterLink.text();

            int finalI = i;
            tasks.add(() -> {
                try {
                    ChapterDTO chapterDTO = new ChapterDTO();
                    chapterDTO.setChapterTitle(chapterName);
                    chapterDTO.setChapterId((long) (finalI + 1));  // Set chapter order
                    chapterDTO.fetchContent(chapterUrl);  // Fetch content

                    // Check if the content was successfully fetched
                    if (chapterDTO.getContent() != null && !chapterDTO.getContent().isEmpty()) {
                        logger.info("成功获取章节: {}", chapterName);
                        chapters.add(chapterDTO);
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
                    ChapterDTO retryChapter = new ChapterDTO();
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
