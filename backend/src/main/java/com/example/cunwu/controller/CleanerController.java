
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Cleaner;
import com.example.cunwu.mapper.CleanerMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cleaner")
public class CleanerController {

    private final CleanerMapper cleanerMapper;

    public CleanerController(CleanerMapper cleanerMapper) {
        this.cleanerMapper = cleanerMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Cleaner>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Cleaner> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Cleaner::getName, keyword)
                    .or().like(Cleaner::getPhone, keyword)
                    .or().like(Cleaner::getArea, keyword));
        }
        wrapper.orderByDesc(Cleaner::getCreateTime);
        return Result.success(cleanerMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Cleaner> getById(@PathVariable Long id) {
        return Result.success(cleanerMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Cleaner> add(@RequestBody Cleaner cleaner) {
        cleaner.setCreateTime(LocalDateTime.now());
        cleanerMapper.insert(cleaner);
        return Result.success("新增成功", cleaner);
    }

    @PutMapping("/update")
    public Result<Cleaner> update(@RequestBody Cleaner cleaner) {
        cleanerMapper.updateById(cleaner);
        return Result.success("更新成功", cleaner);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        cleanerMapper.deleteById(id);
        return Result.success();
    }
}
