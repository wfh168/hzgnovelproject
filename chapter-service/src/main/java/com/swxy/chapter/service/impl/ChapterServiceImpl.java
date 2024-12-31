package com.swxy.chapter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swxy.api.client.NovelClient;
import com.swxy.api.dto.NovelDTO;
import com.swxy.chapter.domain.dto.ChapterDTO;
import com.swxy.chapter.domain.po.Chapter;
import com.swxy.chapter.mapper.ChapterMapper;
import com.swxy.chapter.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 章节服务实现类
 * <p>
 * 提供了操作章节数据的方法，包括将章节插入数据库、根据小说ID或名称获取章节标题等功能。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    private static final Logger logger = LoggerFactory.getLogger(ChapterServiceImpl.class);
    private final ChapterMapper chapterMapper;
    private final NovelClient novelClient;

    /**
     * 将章节列表插入到数据库中
     * <p>
     * 如果章节的标题或内容为空，跳过该章节的插入。
     * </p>
     *
     * @param chapters 章节列表
     * @param novelId  小说ID，用于标识章节属于哪本小说
     */
    @Override
    public void insertChaptersToDatabase(List<ChapterDTO> chapters, Long novelId) {
        for (ChapterDTO chapterDTO : chapters) {
            // 检查章节标题和内容是否为空
            if (chapterDTO.getChapterTitle() == null || chapterDTO.getContent() == null) {
                logger.warn("章节 '{}' 内容或标题为空,跳过插入", chapterDTO.getChapterTitle());
                logger.warn("章节{}跳过插入", chapterDTO.getChapterId());
                continue; // 跳过这个章节
            }
            // 设置小说ID
            chapterDTO.setNovelId(novelId);
            System.out.println(novelId);
            // 插入章节
            chapterMapper.insertChapter(chapterDTO);
            System.out.println("插入成功: " + chapterDTO.getChapterTitle());
        }
    }

    /**
     * 根据小说ID或小说名称获取该小说的章节标题列表
     * <p>
     * 如果提供了小说ID，直接根据小说ID查询章节标题。如果提供了小说名称，先根据小说名称查询小说ID，
     * 然后再根据小说ID查询章节标题。
     * </p>
     *
     * @param novelId   小说ID
     * @param novelName 小说名称
     * @return 章节标题列表
     * @throws RuntimeException 如果未找到对应的小说，或者小说ID与小说名称不匹配，抛出异常
     */
    @Override
    public List<String> getChapterTitles(Long novelId, String novelName) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();

        // 如果提供了小说ID，直接通过小说ID查询章节
        if (novelId != null) {
            queryWrapper.eq("novel_id", novelId);
        }

        // 如果提供了小说名称，先根据小说名称查询小说ID
        if (novelName != null) {

            NovelDTO novelDTO = (NovelDTO) novelClient.getNovelsByTitle(novelName);

            if (novelDTO == null) {
                // 如果没有找到对应的小说，抛出异常
                throw new RuntimeException("小说名称不存在！");
            }

            // 如果提供了 novelName，检查输入的 novelId 是否与查询到的小说ID一致
            if (novelId != null && !novelId.equals(novelDTO.getId())) {
                // 如果 ID 不匹配，抛出异常
                throw new RuntimeException("提供的小说ID与小说名称不匹配！");
            }

            // 使用查询到的 novelId 来查询章节
            queryWrapper.eq("novel_id", novelDTO.getId());
        }

        // 查询并返回章节标题
        List<Chapter> chapters = chapterMapper.selectList(queryWrapper);
        return chapters.stream()
                .map(Chapter::getChapterTitle)
                .collect(Collectors.toList());
    }

    /**
     * 根据小说ID和章节ID获取章节信息
     * <p>
     * 通过查询数据库，根据小说ID和章节ID返回对应的章节数据。
     * </p>
     *
     * @param novelId   小说ID
     * @param chapterId 章节ID
     * @return 章节信息，如果没有找到章节，返回null
     */
    @Override
    public Chapter getChapterByNovelAndChapterId(Long novelId, Long chapterId) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("novel_id", novelId)
                .eq("chapter_id", chapterId);

        // 查询并返回章节信息
        return chapterMapper.selectOne(queryWrapper);
    }
}
