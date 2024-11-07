package com.swxy.crawl;


import com.swxy.entity.Chapter;
import com.swxy.entity.Novel;
import com.swxy.entity.NovelsStart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovelsCrawler {
    private static final Logger logger = LoggerFactory.getLogger(NovelsCrawler.class);

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("请输入小说名称: ");
            String novelName = scanner.nextLine();
            searchNovel(novelName);
        } catch (IOException e) {
            logger.error("发生错误: {}", e.getMessage());
        }
    }

    private static void searchNovel(String novelName) throws IOException {
        NovelsStart novelsStart = new NovelsStart();
        Document searchPage = Jsoup.connect(novelsStart.getSearchUrl())
                .header("User-Agent", novelsStart.getUserAgent())
                .data("searchkey", novelName)
                .post();

        Map<String, String> novelMap = new HashMap<>();
        Elements novelLinks = searchPage.select("h4 > a");

        for (Element link : novelLinks) {
            novelMap.put(link.text(), link.absUrl("href"));
        }

        if (novelMap.isEmpty()) {
            System.out.println("未找到相关小说。");
            return;
        }

        displayNovels(novelMap);
        selectNovel(novelMap);
    }

    private static void displayNovels(Map<String, String> novelMap) {
        System.out.println("找到的小说：");
        int index = 1;
        for (Map.Entry<String, String> entry : novelMap.entrySet()) {
            System.out.println(index++ + ". " + entry.getKey() + " : " + entry.getValue());
        }
    }

    private static void selectNovel(Map<String, String> novelMap) throws IOException {
        System.out.print("请输入小说编号: ");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = scanner.nextInt();
        String novelUrl = (String) novelMap.values().toArray()[selectedIndex - 1];
        if (novelUrl != null) {
            fetchNovelDetails(novelUrl);
        } else {
            System.out.println("未找到该小说标题。");
        }
    }

    private static void fetchNovelDetails(String novelUrl) throws IOException {
        NovelsStart novelsStart = new NovelsStart();
        Document novelPage = Jsoup.connect(novelUrl)
                .header("User-Agent", novelsStart.getUserAgent())
                .get();

        Novel novel = new Novel(novelPage);
        logger.info(novel.toString());

        ImageDownloader.downloadImage(novelPage, novelsStart.getSaveDir());
        fetchChapters(novelPage);
    }

    private static void fetchChapters(Document novelPage) {
        Elements chapterLinks = novelPage.select("#newlist a");
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (Element chapterLink : chapterLinks) {
            String chapterUrl = chapterLink.absUrl("href");
            String chapterName = chapterLink.text();
            executor.submit(() -> {
                Chapter chapter = new Chapter(chapterUrl);
                chapter.fetchContent();
                logger.info("章节: {}", chapterName);
                logger.info("章节内容: {}", chapter.getContent());
                System.out.println("-------------------------------------------------");
            });
        }
        executor.shutdown();
    }
}

