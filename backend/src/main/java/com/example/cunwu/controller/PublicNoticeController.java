
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.PublicNotice;
import com.example.cunwu.mapper.PublicNoticeMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public-notice")
public class PublicNoticeController {

    private final PublicNoticeMapper publicNoticeMapper;

    public PublicNoticeController(PublicNoticeMapper publicNoticeMapper) {
        this.publicNoticeMapper = publicNoticeMapper;
    }

    @GetMapping("/list")
    public Result<IPage<PublicNotice>> getPublicNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String noticeType) {
        
        LambdaQueryWrapper<PublicNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PublicNotice::getStatus, 1);
        if (noticeType != null && !noticeType.isEmpty()) {
            wrapper.eq(PublicNotice::getNoticeType, noticeType);
        }
        wrapper.orderByDesc(PublicNotice::getCreateTime);
        
        return Result.success(publicNoticeMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<PublicNotice> getPublicNoticeById(@PathVariable Long id) {
        return Result.success(publicNoticeMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<PublicNotice> addPublicNotice(@RequestBody PublicNotice notice) {
        notice.setStatus(1);
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        publicNoticeMapper.insert(notice);
        return Result.success("添加成功", notice);
    }

    @PutMapping("/update")
    public Result<PublicNotice> updatePublicNotice(@RequestBody PublicNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        publicNoticeMapper.updateById(notice);
        return Result.success("更新成功", notice);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePublicNotice(@PathVariable Long id) {
        publicNoticeMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        PublicNotice notice = publicNoticeMapper.selectById(id);
        if (notice != null) {
            notice.setStatus(status);
            publicNoticeMapper.updateById(notice);
        }
        return Result.success();
    }
}
