package com.swxy.novel.controller;

import com.swxy.common.exception.BadRequestException;
import com.swxy.novel.entity.po.Chapter;
import com.swxy.novel.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chapter")
@Api(tags = "小说 API") // 为控制器添加 API 标签
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    /**
     * 根据小说ID或小说名称获取章节标题
     * @param novelId 小说ID，可以为空
     * @param novelName 小说名称，可以为空
     * @return 章节标题列表
     */
    @GetMapping("/chapters/search")
    @ApiOperation(value = "根据小说ID或小说名称查看所有章节标题", notes = "根据小说ID或小说名称查询该小说的所有章节名称")
    public ResponseEntity<?> searchChapters(@RequestParam(required = false) Long novelId,
                                            @RequestParam(required = false) String novelName) {
        if (novelId == null && (novelName == null || novelName.isEmpty())) {
            // 如果都没有传入小说ID和小说名称，则抛出BadRequestException
            throw new BadRequestException("小说ID和小说名称不能都为空");
        }

        try {
            List<String> chapterTitles = chapterService.getChapterTitles(novelId, novelName);
            return ResponseEntity.ok(chapterTitles);
        } catch (RuntimeException e) {
            // 捕获运行时异常（例如，小说ID和小说名称不匹配）
            throw new BadRequestException("无法根据提供的信息查询章节: " + e.getMessage(), e);
        }
    }

    @ApiOperation("根据小说ID和章节ID获取章节信息")
    @GetMapping("/chapters/{novelId}/{chapterId}")
    public ResponseEntity<?> getChapter(@PathVariable Long novelId, @PathVariable Long chapterId) {
        if (novelId == null || chapterId == null) {
            // 如果小说ID或章节ID为空，抛出 BadRequestException 异常
            throw new BadRequestException("小说ID和章节ID不能为空");
        }

        try {
            // 查询章节信息
            Chapter chapter = chapterService.getChapterByNovelAndChapterId(novelId, chapterId);

            // 如果没有找到章节，则抛出异常
            if (chapter == null) {
                throw new BadRequestException("没有找到对应的章节信息");
            }

            return ResponseEntity.ok(chapter);
        } catch (BadRequestException e) {
            // 直接抛出 BadRequestException
            throw e;
        } catch (Exception e) {
            // 捕获其他异常，统一处理
            throw new BadRequestException("查询章节信息时发生了错误: " + e.getMessage(), e);
        }
    }
}