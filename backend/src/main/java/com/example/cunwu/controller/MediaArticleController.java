
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.MediaArticle;
import com.example.cunwu.mapper.MediaArticleMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/media-article")
public class MediaArticleController {

    private final MediaArticleMapper mediaArticleMapper;

    public MediaArticleController(MediaArticleMapper mediaArticleMapper) {
        this.mediaArticleMapper = mediaArticleMapper;
    }

    @GetMapping("/list")
    public Result<IPage<MediaArticle>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<MediaArticle> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MediaArticle::getTitle, keyword)
                    .or().like(MediaArticle::getArticleType, keyword)
                    .or().like(MediaArticle::getContent, keyword)
                    .or().like(MediaArticle::getAuthor, keyword));
        }
        wrapper.orderByDesc(MediaArticle::getCreateTime);
        return Result.success(mediaArticleMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<MediaArticle> getById(@PathVariable Long id) {
        return Result.success(mediaArticleMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<MediaArticle> add(@RequestBody MediaArticle mediaArticle) {
        mediaArticle.setCreateTime(LocalDateTime.now());
        mediaArticleMapper.insert(mediaArticle);
        return Result.success("新增成功", mediaArticle);
    }

    @PutMapping("/update")
    public Result<MediaArticle> update(@RequestBody MediaArticle mediaArticle) {
        mediaArticleMapper.updateById(mediaArticle);
        return Result.success("更新成功", mediaArticle);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mediaArticleMapper.deleteById(id);
        return Result.success();
    }
}
