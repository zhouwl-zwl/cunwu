
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Environment;
import com.example.cunwu.mapper.EnvironmentMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/environment")
public class EnvironmentController {

    private final EnvironmentMapper environmentMapper;

    public EnvironmentController(EnvironmentMapper environmentMapper) {
        this.environmentMapper = environmentMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Environment>> getEnvironments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String groupName) {
        
        LambdaQueryWrapper<Environment> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Environment::getType, type);
        }
        if (groupName != null && !groupName.isEmpty()) {
            wrapper.eq(Environment::getGroupName, groupName);
        }
        wrapper.orderByDesc(Environment::getCreateTime);
        
        return Result.success(environmentMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Environment> getEnvironmentById(@PathVariable Long id) {
        return Result.success(environmentMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Environment> addEnvironment(@RequestBody Environment environment) {
        environment.setCreateTime(LocalDateTime.now());
        environmentMapper.insert(environment);
        return Result.success("添加成功", environment);
    }

    @PutMapping("/update")
    public Result<Environment> updateEnvironment(@RequestBody Environment environment) {
        environmentMapper.updateById(environment);
        return Result.success("更新成功", environment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteEnvironment(@PathVariable Long id) {
        environmentMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/inspection")
    public Result<IPage<Environment>> getInspectionList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Environment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Environment::getType, "inspection");
        wrapper.orderByDesc(Environment::getCreateTime);
        return Result.success(environmentMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/courtyard")
    public Result<IPage<Environment>> getCourtyardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Environment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Environment::getType, "courtyard");
        wrapper.orderByDesc(Environment::getCreateTime);
        return Result.success(environmentMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/cleaner")
    public Result<IPage<Environment>> getCleanerList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Environment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Environment::getType, "cleaner");
        wrapper.orderByDesc(Environment::getCreateTime);
        return Result.success(environmentMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/facility")
    public Result<IPage<Environment>> getFacilityList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Environment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Environment::getType, "facility");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Environment::getTitle, keyword)
                    .or().like(Environment::getContent, keyword));
        }
        wrapper.orderByDesc(Environment::getCreateTime);
        return Result.success(environmentMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
