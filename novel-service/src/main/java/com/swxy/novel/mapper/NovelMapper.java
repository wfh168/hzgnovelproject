package com.swxy.novel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swxy.novel.entity.po.Novel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NovelMapper extends BaseMapper<Novel> {
    List<Novel> getAllNovels();
}
