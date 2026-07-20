
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Archive;
import com.example.cunwu.mapper.ArchiveMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/archive")
public class ArchiveController {

    private final ArchiveMapper archiveMapper;

    public ArchiveController(ArchiveMapper archiveMapper) {
        this.archiveMapper = archiveMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Archive>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword)
                    .or().like(Archive::getCategory, keyword)
                    .or().like(Archive::getSource, keyword)
                    .or().like(Archive::getContent, keyword)
                    .or().like(Archive::getKeywords, keyword));
        }
        wrapper.orderByDesc(Archive::getCreateTime);
        return Result.success(archiveMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Archive> getById(@PathVariable Long id) {
        return Result.success(archiveMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Archive> add(@RequestBody Archive archive) {
        archive.setCreateTime(LocalDateTime.now());
        archiveMapper.insert(archive);
        return Result.success("新增成功", archive);
    }

    @PutMapping("/update")
    public Result<Archive> update(@RequestBody Archive archive) {
        archiveMapper.updateById(archive);
        return Result.success("更新成功", archive);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        archiveMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/party")
    public Result<IPage<Archive>> getPartyArchives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getCategory, "party");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword)
                    .or().like(Archive::getContent, keyword));
        }
        wrapper.orderByDesc(Archive::getCreateTime);
        return Result.success(archiveMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/finance")
    public Result<IPage<Archive>> getFinanceArchives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getCategory, "finance");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword)
                    .or().like(Archive::getContent, keyword));
        }
        wrapper.orderByDesc(Archive::getCreateTime);
        return Result.success(archiveMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/population")
    public Result<IPage<Archive>> getPopulationArchives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getCategory, "population");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword)
                    .or().like(Archive::getContent, keyword));
        }
        wrapper.orderByDesc(Archive::getCreateTime);
        return Result.success(archiveMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/project")
    public Result<IPage<Archive>> getProjectArchives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getCategory, "project");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword)
                    .or().like(Archive::getContent, keyword));
        }
        wrapper.orderByDesc(Archive::getCreateTime);
        return Result.success(archiveMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
