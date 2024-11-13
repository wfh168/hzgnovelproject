package com.swxy.api.client;

import com.swxy.api.dto.NovelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "novel-service")
public interface NovelClient {
    @GetMapping("/api/novel/searchByName")
    List<NovelDTO> getNovelsByTitle(@RequestParam("title") String title);
}
