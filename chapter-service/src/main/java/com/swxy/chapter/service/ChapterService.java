package com.swxy.chapter.service;



import com.swxy.chapter.domain.dto.ChapterDTO;
import com.swxy.chapter.domain.po.Chapter;

import java.util.List;

public interface ChapterService {
    void insertChaptersToDatabase(List<ChapterDTO> chapters, Long novelId);
    /**
     * 根据小说ID或小说名称获取章节标题
     * @param novelId 小说ID，可以为null
     * @param novelName 小说名称，可以为null
     * @return 章节标题列表
     */
    List<String> getChapterTitles(Long novelId, String novelName);


    Chapter getChapterByNovelAndChapterId(Long novelId, Long chapterId);
}
