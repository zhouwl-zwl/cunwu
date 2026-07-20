
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.SafetyHazard;
import com.example.cunwu.mapper.SafetyHazardMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/safety-hazard")
public class SafetyHazardController {

    private final SafetyHazardMapper safetyHazardMapper;

    public SafetyHazardController(SafetyHazardMapper safetyHazardMapper) {
        this.safetyHazardMapper = safetyHazardMapper;
    }

    @GetMapping("/list")
    public Result<IPage<SafetyHazard>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<SafetyHazard> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(SafetyHazard::getHazardType, type);
        }
        wrapper.orderByDesc(SafetyHazard::getCreateTime);
        return Result.success(safetyHazardMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<SafetyHazard> getById(@PathVariable Long id) {
        return Result.success(safetyHazardMapper.selectById(id));
    }

    @PostMapping
    public Result<SafetyHazard> add(@RequestBody SafetyHazard safetyHazard) {
        safetyHazard.setCreateTime(LocalDateTime.now());
        safetyHazardMapper.insert(safetyHazard);
        return Result.success("添加成功", safetyHazard);
    }

    @PutMapping
    public Result<SafetyHazard> update(@RequestBody SafetyHazard safetyHazard) {
        safetyHazardMapper.updateById(safetyHazard);
        return Result.success("更新成功", safetyHazard);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        safetyHazardMapper.deleteById(id);
        return Result.success();
    }
}
