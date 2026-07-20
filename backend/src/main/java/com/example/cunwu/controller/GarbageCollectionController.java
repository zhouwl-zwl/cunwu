
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.GarbageCollection;
import com.example.cunwu.mapper.GarbageCollectionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/garbage-collection")
public class GarbageCollectionController {

    private final GarbageCollectionMapper garbageCollectionMapper;

    public GarbageCollectionController(GarbageCollectionMapper garbageCollectionMapper) {
        this.garbageCollectionMapper = garbageCollectionMapper;
    }

    @GetMapping("/list")
    public Result<IPage<GarbageCollection>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<GarbageCollection> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(GarbageCollection::getCleanerName, type);
        }
        wrapper.orderByDesc(GarbageCollection::getCreateTime);
        return Result.success(garbageCollectionMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<GarbageCollection> getById(@PathVariable Long id) {
        return Result.success(garbageCollectionMapper.selectById(id));
    }

    @PostMapping
    public Result<GarbageCollection> add(@RequestBody GarbageCollection garbageCollection) {
        garbageCollection.setCreateTime(LocalDateTime.now());
        garbageCollectionMapper.insert(garbageCollection);
        return Result.success("添加成功", garbageCollection);
    }

    @PutMapping
    public Result<GarbageCollection> update(@RequestBody GarbageCollection garbageCollection) {
        garbageCollectionMapper.updateById(garbageCollection);
        return Result.success("更新成功", garbageCollection);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        garbageCollectionMapper.deleteById(id);
        return Result.success();
    }
}
