package com.swxy.novel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swxy.novel.entity.po.Chapter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
}