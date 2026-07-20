
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Subsidy;
import com.example.cunwu.mapper.SubsidyMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/subsidy")
public class SubsidyController {

    private final SubsidyMapper subsidyMapper;

    public SubsidyController(SubsidyMapper subsidyMapper) {
        this.subsidyMapper = subsidyMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Subsidy>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Subsidy> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Subsidy::getSubsidyType, keyword)
                    .or().like(Subsidy::getRecipientName, keyword)
                    .or().like(Subsidy::getRecipientIdCard, keyword)
                    .or().like(Subsidy::getRecipientPhone, keyword)
                    .or().like(Subsidy::getBankCard, keyword));
        }
        wrapper.orderByDesc(Subsidy::getCreateTime);
        return Result.success(subsidyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Subsidy> getById(@PathVariable Long id) {
        return Result.success(subsidyMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Subsidy> add(@RequestBody Subsidy subsidy) {
        subsidy.setCreateTime(LocalDateTime.now());
        subsidyMapper.insert(subsidy);
        return Result.success("新增成功", subsidy);
    }

    @PutMapping("/update")
    public Result<Subsidy> update(@RequestBody Subsidy subsidy) {
        subsidyMapper.updateById(subsidy);
        return Result.success("更新成功", subsidy);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        subsidyMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<IPage<Subsidy>> getMySubsidies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Subsidy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Subsidy::getCreateTime);
        return Result.success(subsidyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/all")
    public Result<IPage<Subsidy>> getAllSubsidies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Subsidy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Subsidy::getCreateTime);
        return Result.success(subsidyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/public")
    public Result<IPage<Subsidy>> getPublicSubsidies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Subsidy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Subsidy::getCreateTime);
        return Result.success(subsidyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/abnormal")
    public Result<IPage<Subsidy>> getAbnormalSubsidies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Subsidy> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Subsidy::getCreateTime);
        return Result.success(subsidyMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
