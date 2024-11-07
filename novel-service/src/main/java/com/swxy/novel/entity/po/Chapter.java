package com.swxy.novel.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

@Data
@ApiModel(description = "小说信息实体类")
@TableName("chapters")
public class Chapter {

    @ApiModelProperty(value = "主键，自增的章节ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "指向所属小说的ID", required = true)
    private Long novelId;

    @ApiModelProperty(value = "章节标题", required = true)
    private String title;

    @ApiModelProperty(value = "章节内容", required = true)
    private String content;

    @ApiModelProperty(value = "章节最后更新时间", required = true)
    private Date updateTime;

    @ApiModelProperty(value = "小说章节id", required = true)
    private Long chapterId;

    /**
     * Fetches the content of a chapter from the provided URL.
     * @param url The URL of the chapter to fetch.
     */
    public void fetchContent(String url) {
        try {
            Document chapterPage = Jsoup.connect(url).get(); // Fetch the chapter page
            Elements paragraphs = chapterPage.select("#booktxt p"); // Select paragraphs in the content area
            StringBuilder chapterContent = new StringBuilder();
            paragraphs.forEach(paragraph -> chapterContent.append(paragraph.outerHtml()));

            // Clean up unwanted content
            this.content = cleanUpContent(chapterContent.toString());

            // Set update time to the current time when content is fetched
            this.updateTime = new Date();
        } catch (IOException e) {
            throw new ChapterFetchException("Failed to fetch chapter content from URL: " + url, e);
        }
    }

    /**
     * Cleans up the content by removing unwanted paragraphs or text.
     * @param content The raw content to clean.
     * @return Cleaned content.
     */
    private String cleanUpContent(String content) {
        // Remove unwanted content such as the "please disable reading mode" message.
        return content.replace("<p>请关闭浏览器的阅读/畅读/小说模式并且关闭广告屏蔽过滤功能，避免出现内容无法显示或者段落错乱。</p>", "").trim();
    }

    /**
     * Custom exception for errors during chapter content fetching.
     */
    public static class ChapterFetchException extends RuntimeException {
        public ChapterFetchException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
