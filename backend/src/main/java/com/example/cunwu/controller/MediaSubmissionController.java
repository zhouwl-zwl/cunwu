
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.MediaSubmission;
import com.example.cunwu.mapper.MediaSubmissionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/media-submission")
public class MediaSubmissionController {

    private final MediaSubmissionMapper mediaSubmissionMapper;

    public MediaSubmissionController(MediaSubmissionMapper mediaSubmissionMapper) {
        this.mediaSubmissionMapper = mediaSubmissionMapper;
    }

    @GetMapping("/list")
    public Result<IPage<MediaSubmission>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<MediaSubmission> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MediaSubmission::getTitle, keyword)
                    .or().like(MediaSubmission::getTargetMedia, keyword)
                    .or().like(MediaSubmission::getContent, keyword));
        }
        wrapper.orderByDesc(MediaSubmission::getCreateTime);
        return Result.success(mediaSubmissionMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<MediaSubmission> getById(@PathVariable Long id) {
        return Result.success(mediaSubmissionMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<MediaSubmission> add(@RequestBody MediaSubmission mediaSubmission) {
        mediaSubmission.setCreateTime(LocalDateTime.now());
        mediaSubmissionMapper.insert(mediaSubmission);
        return Result.success("新增成功", mediaSubmission);
    }

    @PutMapping("/update")
    public Result<MediaSubmission> update(@RequestBody MediaSubmission mediaSubmission) {
        mediaSubmissionMapper.updateById(mediaSubmission);
        return Result.success("更新成功", mediaSubmission);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mediaSubmissionMapper.deleteById(id);
        return Result.success();
    }
}
