
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.ArchiveRecord;
import com.example.cunwu.mapper.ArchiveRecordMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/archive-record")
public class ArchiveRecordController {

    private final ArchiveRecordMapper archiveRecordMapper;

    public ArchiveRecordController(ArchiveRecordMapper archiveRecordMapper) {
        this.archiveRecordMapper = archiveRecordMapper;
    }

    @GetMapping("/list")
    public Result<IPage<ArchiveRecord>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<ArchiveRecord> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ArchiveRecord::getCategory, type);
        }
        wrapper.orderByDesc(ArchiveRecord::getCreateTime);
        return Result.success(archiveRecordMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<ArchiveRecord> getById(@PathVariable Long id) {
        return Result.success(archiveRecordMapper.selectById(id));
    }

    @PostMapping
    public Result<ArchiveRecord> add(@RequestBody ArchiveRecord archiveRecord) {
        archiveRecord.setCreateTime(LocalDateTime.now());
        archiveRecordMapper.insert(archiveRecord);
        return Result.success("添加成功", archiveRecord);
    }

    @PutMapping
    public Result<ArchiveRecord> update(@RequestBody ArchiveRecord archiveRecord) {
        archiveRecordMapper.updateById(archiveRecord);
        return Result.success("更新成功", archiveRecord);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        archiveRecordMapper.deleteById(id);
        return Result.success();
    }
}
