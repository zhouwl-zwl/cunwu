
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.DocumentLibrary;
import com.example.cunwu.mapper.DocumentLibraryMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/document-library")
public class DocumentLibraryController {

    private final DocumentLibraryMapper documentLibraryMapper;

    public DocumentLibraryController(DocumentLibraryMapper documentLibraryMapper) {
        this.documentLibraryMapper = documentLibraryMapper;
    }

    @GetMapping("/list")
    public Result<IPage<DocumentLibrary>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<DocumentLibrary> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(DocumentLibrary::getCategory, type);
        }
        wrapper.orderByDesc(DocumentLibrary::getCreateTime);
        return Result.success(documentLibraryMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<DocumentLibrary> getById(@PathVariable Long id) {
        return Result.success(documentLibraryMapper.selectById(id));
    }

    @PostMapping
    public Result<DocumentLibrary> add(@RequestBody DocumentLibrary documentLibrary) {
        documentLibrary.setCreateTime(LocalDateTime.now());
        documentLibraryMapper.insert(documentLibrary);
        return Result.success("添加成功", documentLibrary);
    }

    @PutMapping
    public Result<DocumentLibrary> update(@RequestBody DocumentLibrary documentLibrary) {
        documentLibraryMapper.updateById(documentLibrary);
        return Result.success("更新成功", documentLibrary);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        documentLibraryMapper.deleteById(id);
        return Result.success();
    }
}
