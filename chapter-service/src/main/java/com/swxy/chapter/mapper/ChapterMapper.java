package com.swxy.chapter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swxy.chapter.domain.dto.ChapterDTO;
import com.swxy.chapter.domain.po.Chapter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
    void insertChapter(ChapterDTO chapterDTO);
}