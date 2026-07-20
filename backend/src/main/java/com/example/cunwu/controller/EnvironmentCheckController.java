
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.EnvironmentCheck;
import com.example.cunwu.mapper.EnvironmentCheckMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/environment-check")
public class EnvironmentCheckController {

    private final EnvironmentCheckMapper environmentCheckMapper;

    public EnvironmentCheckController(EnvironmentCheckMapper environmentCheckMapper) {
        this.environmentCheckMapper = environmentCheckMapper;
    }

    @GetMapping("/list")
    public Result<IPage<EnvironmentCheck>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<EnvironmentCheck> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(EnvironmentCheck::getCheckType, keyword)
                    .or().like(EnvironmentCheck::getLocation, keyword)
                    .or().like(EnvironmentCheck::getDescription, keyword)
                    .or().like(EnvironmentCheck::getInspector, keyword));
        }
        wrapper.orderByDesc(EnvironmentCheck::getCreateTime);
        return Result.success(environmentCheckMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<EnvironmentCheck> getById(@PathVariable Long id) {
        return Result.success(environmentCheckMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<EnvironmentCheck> add(@RequestBody EnvironmentCheck environmentCheck) {
        environmentCheck.setCreateTime(LocalDateTime.now());
        environmentCheckMapper.insert(environmentCheck);
        return Result.success("新增成功", environmentCheck);
    }

    @PutMapping("/update")
    public Result<EnvironmentCheck> update(@RequestBody EnvironmentCheck environmentCheck) {
        environmentCheckMapper.updateById(environmentCheck);
        return Result.success("更新成功", environmentCheck);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        environmentCheckMapper.deleteById(id);
        return Result.success();
    }
}
