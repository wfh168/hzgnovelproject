package com.swxy.novel.crawl;

import com.swxy.novel.domain.po.Novel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class NovelsCrawler {

    private final SearchService searchService;
    private final NovelDetailsService novelDetailsService;

    // Constructor Injection (Best practice in Spring)
    @Autowired
    public NovelsCrawler(SearchService searchService, NovelDetailsService novelDetailsService) {
        this.searchService = searchService;
        this.novelDetailsService = novelDetailsService;
    }

    /**
     * Search novels by name or author.
     * @param novelName The name or author of the novel.
     * @return A map with integer keys (e.g., novel ID) and string values (e.g., novel title or URL).
     * @throws IOException If an error occurs during the search process.
     */
    public Map<Integer, String> searchNovels(String novelName) throws IOException {
        try {
            return searchService.search(novelName);
        } catch (IOException e) {
            throw new IOException("Failed to search novels for: " + novelName, e);
        }
    }

    /**
     * Fetch the details of a specific novel.
     * @param novelUrl The URL of the novel.
     * @param saveDir The directory where the novel details should be saved.
     * @return The novel details.
     * @throws IOException If an error occurs while fetching the details.
     */
    public Novel fetchNovelDetails(String novelUrl, String saveDir) throws IOException {
        try {
            return novelDetailsService.fetchDetails(novelUrl, saveDir);
        } catch (IOException e) {
            throw new IOException("Failed to fetch details for novel: " + novelUrl, e);
        }
    }


}
