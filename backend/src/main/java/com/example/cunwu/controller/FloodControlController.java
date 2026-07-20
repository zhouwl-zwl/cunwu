
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.FloodControl;
import com.example.cunwu.mapper.FloodControlMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/flood-control")
public class FloodControlController {

    private final FloodControlMapper floodControlMapper;

    public FloodControlController(FloodControlMapper floodControlMapper) {
        this.floodControlMapper = floodControlMapper;
    }

    @GetMapping("/list")
    public Result<IPage<FloodControl>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<FloodControl> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(FloodControl::getItemType, type);
        }
        wrapper.orderByDesc(FloodControl::getCreateTime);
        return Result.success(floodControlMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<FloodControl> getById(@PathVariable Long id) {
        return Result.success(floodControlMapper.selectById(id));
    }

    @PostMapping
    public Result<FloodControl> add(@RequestBody FloodControl floodControl) {
        floodControl.setCreateTime(LocalDateTime.now());
        floodControlMapper.insert(floodControl);
        return Result.success("添加成功", floodControl);
    }

    @PutMapping
    public Result<FloodControl> update(@RequestBody FloodControl floodControl) {
        floodControlMapper.updateById(floodControl);
        return Result.success("更新成功", floodControl);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        floodControlMapper.deleteById(id);
        return Result.success();
    }
}
