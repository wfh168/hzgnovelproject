package com.swxy.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swxy.novel.domain.po.Novel;
import com.swxy.novel.mapper.NovelMapper;
import com.swxy.novel.service.NovelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小说服务实现类
 * <p>
 * 提供了操作小说数据的方法，包括将小说插入数据库、获取所有小说以及根据标题搜索小说等功能。
 * </p>
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

    private static final Logger logger = LoggerFactory.getLogger(NovelServiceImpl.class);

    @Autowired
    private NovelMapper novelMapper;

    /**
     * 将小说插入数据库中
     * <p>
     * 该方法首先检查数据库中是否已存在相同标题的小说，如果存在，则跳过插入操作；
     * 如果不存在，则插入新的小说记录。如果插入失败，捕获 `DuplicateKeyException` 异常并记录警告。
     * </p>
     *
     * @param novel 要插入的小说对象
     */
    @Override
    public void insertNovelToDatabase(Novel novel) {
        // 查询数据库中是否已存在相同标题的小说
        Novel existingNovel = novelMapper.selectOne(new QueryWrapper<Novel>().eq("title", novel.getTitle()));
        logger.info("查找小说信息: {}", existingNovel);

        // 如果小说已存在，跳过插入
        if (existingNovel != null) {
            logger.warn("小说 '{}' 已存在，跳过插入。", novel.getTitle());
            return;
        }

        try {
            // 插入小说详情
            novelMapper.insert(novel);
            logger.info("插入小说: {}", novel.getTitle());
        } catch (DuplicateKeyException e) {
            // 捕获插入失败的异常，记录警告
            logger.warn("插入失败，小说 '{}' 已存在。", novel.getTitle());
        } catch (Exception e) {
            // 捕获其他异常，记录错误信息
            logger.error("插入小说 '{}' 时发生错误: {}", novel.getTitle(), e.getMessage());
        }
    }

    /**
     * 获取所有小说列表
     * <p>
     * 查询并返回所有已存在的小说记录。
     * </p>
     *
     * @return 小说列表
     */
    @Override
    public List<Novel> getAllNovels() {
        return novelMapper.getAllNovels();
    }

    /**
     * 根据小说标题搜索小说
     * <p>
     * 该方法支持模糊查询，根据提供的标题进行搜索，返回符合条件的小说列表。
     * </p>
     *
     * @param title 小说标题
     * @return 符合条件的小说列表
     */
    @Override
    public List<Novel> searchNovelsByTitle(String title) {
        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();

        // 使用 LIKE 语法进行模糊查询
        queryWrapper.apply("title LIKE '%' || {0} || '%'", title);

        List<Novel> novels = novelMapper.selectList(queryWrapper);
        return novels;
    }
}
