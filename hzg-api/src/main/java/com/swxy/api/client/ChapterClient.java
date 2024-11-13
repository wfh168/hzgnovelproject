package com.swxy.api.client;

import com.swxy.api.dto.ChapterDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "chapter-service")
public interface ChapterClient {
    @PostMapping("/api/chapter/insert")
    String insertChapters(@RequestBody List<ChapterDTO> chapters, @RequestParam("novelId") Long novelId);
    @GetMapping("/api/chapter/fetch")
    List<ChapterDTO> fetchChapters(@RequestParam("url") String url);
}
