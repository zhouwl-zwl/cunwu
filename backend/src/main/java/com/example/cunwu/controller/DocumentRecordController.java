
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.DocumentRecord;
import com.example.cunwu.mapper.DocumentRecordMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/document-record")
public class DocumentRecordController {

    private final DocumentRecordMapper documentRecordMapper;

    public DocumentRecordController(DocumentRecordMapper documentRecordMapper) {
        this.documentRecordMapper = documentRecordMapper;
    }

    @GetMapping("/list")
    public Result<IPage<DocumentRecord>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<DocumentRecord> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(DocumentRecord::getDocType, type);
        }
        wrapper.orderByDesc(DocumentRecord::getCreateTime);
        return Result.success(documentRecordMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<DocumentRecord> getById(@PathVariable Long id) {
        return Result.success(documentRecordMapper.selectById(id));
    }

    @PostMapping
    public Result<DocumentRecord> add(@RequestBody DocumentRecord documentRecord) {
        documentRecord.setCreateTime(LocalDateTime.now());
        documentRecordMapper.insert(documentRecord);
        return Result.success("添加成功", documentRecord);
    }

    @PutMapping
    public Result<DocumentRecord> update(@RequestBody DocumentRecord documentRecord) {
        documentRecordMapper.updateById(documentRecord);
        return Result.success("更新成功", documentRecord);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        documentRecordMapper.deleteById(id);
        return Result.success();
    }
}
