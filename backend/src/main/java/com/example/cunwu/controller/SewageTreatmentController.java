
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.SewageTreatment;
import com.example.cunwu.mapper.SewageTreatmentMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sewage-treatment")
public class SewageTreatmentController {

    private final SewageTreatmentMapper sewageTreatmentMapper;

    public SewageTreatmentController(SewageTreatmentMapper sewageTreatmentMapper) {
        this.sewageTreatmentMapper = sewageTreatmentMapper;
    }

    @GetMapping("/list")
    public Result<IPage<SewageTreatment>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<SewageTreatment> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(SewageTreatment::getStatus, type);
        }
        wrapper.orderByDesc(SewageTreatment::getCreateTime);
        return Result.success(sewageTreatmentMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<SewageTreatment> getById(@PathVariable Long id) {
        return Result.success(sewageTreatmentMapper.selectById(id));
    }

    @PostMapping
    public Result<SewageTreatment> add(@RequestBody SewageTreatment sewageTreatment) {
        sewageTreatment.setCreateTime(LocalDateTime.now());
        sewageTreatmentMapper.insert(sewageTreatment);
        return Result.success("添加成功", sewageTreatment);
    }

    @PutMapping
    public Result<SewageTreatment> update(@RequestBody SewageTreatment sewageTreatment) {
        sewageTreatmentMapper.updateById(sewageTreatment);
        return Result.success("更新成功", sewageTreatment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sewageTreatmentMapper.deleteById(id);
        return Result.success();
    }
}
