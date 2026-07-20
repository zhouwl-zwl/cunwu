
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.ForestFire;
import com.example.cunwu.mapper.ForestFireMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/forest-fire")
public class ForestFireController {

    private final ForestFireMapper forestFireMapper;

    public ForestFireController(ForestFireMapper forestFireMapper) {
        this.forestFireMapper = forestFireMapper;
    }

    @GetMapping("/list")
    public Result<IPage<ForestFire>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<ForestFire> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ForestFire::getPatrolPerson, type);
        }
        wrapper.orderByDesc(ForestFire::getCreateTime);
        return Result.success(forestFireMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<ForestFire> getById(@PathVariable Long id) {
        return Result.success(forestFireMapper.selectById(id));
    }

    @PostMapping
    public Result<ForestFire> add(@RequestBody ForestFire forestFire) {
        forestFire.setCreateTime(LocalDateTime.now());
        forestFireMapper.insert(forestFire);
        return Result.success("添加成功", forestFire);
    }

    @PutMapping
    public Result<ForestFire> update(@RequestBody ForestFire forestFire) {
        forestFireMapper.updateById(forestFire);
        return Result.success("更新成功", forestFire);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        forestFireMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/level")
    public Result<Map<String, Object>> getLevel() {
        Map<String, Object> data = new HashMap<>();
        data.put("level", "三级");
        data.put("message", "当前森林火险等级为三级");
        return Result.success(data);
    }
}
