package com.swxy.chapter.crawl;

import com.swxy.chapter.domain.dto.ChapterDTO;
import com.swxy.chapter.domain.po.Chapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
public class ChaptersCrawler {
    private final FetchChapterService fetchChapterService;

    public ChaptersCrawler(FetchChapterService fetchChapterService) {
        this.fetchChapterService = fetchChapterService;
    }

    /**
     * Fetch all chapters of a specific novel.
     * @param novelUrl The URL of the novel.
     * @return A list of chapters for the novel.
     * @throws IOException If an error occurs while fetching the chapters.
     */
    public List<ChapterDTO> fetchNovelChapters(String novelUrl) throws IOException {
        try {
            Document novelPage = Jsoup.connect(novelUrl).get(); // Fetch the novel page
            return fetchChapterService.fetchChapters(novelPage); // Fetch chapters from the page
        } catch (IOException e) {
            throw new IOException("Failed to fetch chapters for novel: " + novelUrl, e);
        }
    }
}
