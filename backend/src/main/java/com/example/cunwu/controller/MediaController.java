package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.MediaArticle;
import com.example.cunwu.mapper.MediaArticleMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaArticleMapper mediaArticleMapper;

    public MediaController(MediaArticleMapper mediaArticleMapper) {
        this.mediaArticleMapper = mediaArticleMapper;
    }

    @GetMapping("/news")
    public Result<IPage<MediaArticle>> getNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<MediaArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MediaArticle::getArticleType, "news");
        wrapper.orderByDesc(MediaArticle::getCreateTime);
        return Result.success(mediaArticleMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/gallery")
    public Result<IPage<MediaArticle>> getGallery(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<MediaArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MediaArticle::getArticleType, "gallery");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MediaArticle::getTitle, keyword)
                    .or().like(MediaArticle::getContent, keyword));
        }
        wrapper.orderByDesc(MediaArticle::getCreateTime);
        return Result.success(mediaArticleMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/policy")
    public Result<IPage<MediaArticle>> getPolicy(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<MediaArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MediaArticle::getArticleType, "policy");
        wrapper.orderByDesc(MediaArticle::getCreateTime);
        return Result.success(mediaArticleMapper.selectPage(new Page<>(page, size), wrapper));
    }
}