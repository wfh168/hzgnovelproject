package com.swxy.novel.service;

import com.swxy.novel.domain.dto.NovelDTO;
import com.swxy.novel.domain.po.Novel;

import java.util.List;

public interface NovelService {
    void insertNovelToDatabase(Novel novel);
    List<Novel> getAllNovels();
    List<Novel> searchNovelsByTitle(String title);
    List<NovelDTO> getNovelsByTitle(String title);
}
