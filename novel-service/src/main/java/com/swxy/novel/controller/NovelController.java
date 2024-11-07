package com.swxy.novel.controller;

import com.swxy.novel.crawl.NovelsCrawler;
import com.swxy.novel.entity.po.Chapter;
import com.swxy.novel.entity.po.Novel;
import com.swxy.novel.entity.po.NovelsStart;
import com.swxy.novel.service.ChapterService;
import com.swxy.novel.service.NovelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/novel")
@Api(tags = "小说 API") // 为控制器添加 API 标签
public class NovelController {
    private static final Logger logger = LoggerFactory.getLogger(NovelController.class);

    @Autowired
    private NovelsCrawler novelsCrawler;
    @Autowired
    private NovelService novelService;
    @Autowired
    private ChapterService chapterService;

    @PostMapping("/insertSearch")
    @ApiOperation(value = "获取爬取的小说列表", notes = "根据小说名或者作者名获得小说列表")
    public Map<Integer, String> searchNovels(@RequestBody Map<String, String> request) throws IOException {
        String novelName = request.get("novelName");
        return novelsCrawler.searchNovels(novelName);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有小说列表", notes = "返回数据库中所有的小说信息")
    public List<Novel> getAllNovels() {
        return novelService.getAllNovels();
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据小说名称搜索小说", notes = "通过小说名称查询小说列表")
    public List<Novel> searchNovels(@RequestParam("novelName") String novelName) {
        return novelService.searchNovelsByTitle(novelName);
    }

    @PostMapping("/fetch/{key}")
    @ApiOperation(value = "插入小说和章节详情", notes = "根据选择的小说键值获取详情")
    public ResponseEntity<String> fetchNovelDetails(@PathVariable Integer key, @RequestBody Map<Integer, String> novelMap) {
        String url = novelMap.get(key);
        if (url == null) {
            logger.error("未找到与键值 {} 相关的小说 URL", key);
            return ResponseEntity.badRequest().body("无效的键值: " + key);
        }

        try {
            NovelsStart novelsStart = new NovelsStart();
            String saveDir = novelsStart.getSaveDir();
            Novel novel = novelsCrawler.fetchNovelDetails(url, saveDir);
            if (novel == null) {
                logger.error("从 URL '{}' 获取小说详情返回 null", url);
                return ResponseEntity.status(500).body("无法从指定 URL 获取小说详情");
            }

            novelService.insertNovelToDatabase(novel);

            // 获取章节
            List<Chapter> chapters = novelsCrawler.fetchNovelChapters(url);
            if (chapters == null || chapters.isEmpty()) {
                logger.error("获取章节时返回 null 或空列表");
                return ResponseEntity.status(500).body("获取章节失败");
            } else {
                logger.info("获取到 {} 个章节", chapters.size());
                chapterService.insertChaptersToDatabase(chapters, novel.getId());
            }

            return ResponseEntity.ok("小说和章节详情成功插入数据库");

        } catch (IOException e) {
            logger.error("获取小说详情失败: {} - {}", url, e.getMessage());
            return ResponseEntity.status(500).body("获取小说详情时发生错误");
        } catch (Exception e) {
            logger.error("处理小说 {} 时出错: {}", url, e.getMessage(), e);
            return ResponseEntity.status(500).body("处理小说时发生错误");
        }
    }
}
