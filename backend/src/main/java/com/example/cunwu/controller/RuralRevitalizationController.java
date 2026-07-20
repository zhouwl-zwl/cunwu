
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.RuralRevitalization;
import com.example.cunwu.mapper.RuralRevitalizationMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rural-revitalization")
public class RuralRevitalizationController {

    private final RuralRevitalizationMapper ruralRevitalizationMapper;

    public RuralRevitalizationController(RuralRevitalizationMapper ruralRevitalizationMapper) {
        this.ruralRevitalizationMapper = ruralRevitalizationMapper;
    }

    @GetMapping("/list")
    public Result<IPage<RuralRevitalization>> getRuralRevitalizations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String groupName) {
        
        LambdaQueryWrapper<RuralRevitalization> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(RuralRevitalization::getType, type);
        }
        if (groupName != null && !groupName.isEmpty()) {
            wrapper.eq(RuralRevitalization::getGroupName, groupName);
        }
        wrapper.orderByDesc(RuralRevitalization::getCreateTime);
        
        return Result.success(ruralRevitalizationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<RuralRevitalization> getRuralRevitalizationById(@PathVariable Long id) {
        return Result.success(ruralRevitalizationMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<RuralRevitalization> addRuralRevitalization(@RequestBody RuralRevitalization ruralRevitalization) {
        ruralRevitalization.setCreateTime(LocalDateTime.now());
        ruralRevitalizationMapper.insert(ruralRevitalization);
        return Result.success("添加成功", ruralRevitalization);
    }

    @PutMapping("/update")
    public Result<RuralRevitalization> updateRuralRevitalization(@RequestBody RuralRevitalization ruralRevitalization) {
        ruralRevitalizationMapper.updateById(ruralRevitalization);
        return Result.success("更新成功", ruralRevitalization);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRuralRevitalization(@PathVariable Long id) {
        ruralRevitalizationMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/projects")
    public Result<IPage<RuralRevitalization>> getProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<RuralRevitalization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuralRevitalization::getType, "project");
        wrapper.orderByDesc(RuralRevitalization::getCreateTime);
        return Result.success(ruralRevitalizationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/households")
    public Result<IPage<RuralRevitalization>> getHouseholds(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<RuralRevitalization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuralRevitalization::getType, "household");
        wrapper.orderByDesc(RuralRevitalization::getCreateTime);
        return Result.success(ruralRevitalizationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/team")
    public Result<IPage<RuralRevitalization>> getTeam(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<RuralRevitalization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuralRevitalization::getType, "team");
        wrapper.orderByDesc(RuralRevitalization::getCreateTime);
        return Result.success(ruralRevitalizationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/detail/{id}")
    public Result<RuralRevitalization> getDetail(@PathVariable Long id, @RequestParam(required = false) String type) {
        return Result.success(ruralRevitalizationMapper.selectById(id));
    }
}
