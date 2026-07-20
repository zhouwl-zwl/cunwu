
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.SpecialGroup;
import com.example.cunwu.mapper.SpecialGroupMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/special-group")
public class SpecialGroupController {

    private final SpecialGroupMapper specialGroupMapper;

    public SpecialGroupController(SpecialGroupMapper specialGroupMapper) {
        this.specialGroupMapper = specialGroupMapper;
    }

    @GetMapping("/list")
    public Result<IPage<SpecialGroup>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<SpecialGroup> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(SpecialGroup::getGroupType, type);
        }
        wrapper.orderByDesc(SpecialGroup::getCreateTime);
        return Result.success(specialGroupMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<SpecialGroup> getById(@PathVariable Long id) {
        return Result.success(specialGroupMapper.selectById(id));
    }

    @PostMapping
    public Result<SpecialGroup> add(@RequestBody SpecialGroup specialGroup) {
        specialGroup.setCreateTime(LocalDateTime.now());
        specialGroupMapper.insert(specialGroup);
        return Result.success("添加成功", specialGroup);
    }

    @PutMapping
    public Result<SpecialGroup> update(@RequestBody SpecialGroup specialGroup) {
        specialGroupMapper.updateById(specialGroup);
        return Result.success("更新成功", specialGroup);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        specialGroupMapper.deleteById(id);
        return Result.success();
    }
}
