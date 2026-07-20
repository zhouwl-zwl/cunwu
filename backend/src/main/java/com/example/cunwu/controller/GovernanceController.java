
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.ComprehensiveGovernance;
import com.example.cunwu.mapper.ComprehensiveGovernanceMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/governance")
public class GovernanceController {

    private final ComprehensiveGovernanceMapper governanceMapper;

    public GovernanceController(ComprehensiveGovernanceMapper governanceMapper) {
        this.governanceMapper = governanceMapper;
    }

    @GetMapping("/list")
    public Result<IPage<ComprehensiveGovernance>> getGovernanceList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String groupName) {
        
        LambdaQueryWrapper<ComprehensiveGovernance> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ComprehensiveGovernance::getType, type);
        }
        if (groupName != null && !groupName.isEmpty()) {
            wrapper.eq(ComprehensiveGovernance::getGroupName, groupName);
        }
        wrapper.orderByDesc(ComprehensiveGovernance::getCreateTime);
        
        return Result.success(governanceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<ComprehensiveGovernance> getGovernanceById(@PathVariable Long id) {
        return Result.success(governanceMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<ComprehensiveGovernance> addGovernance(@RequestBody ComprehensiveGovernance governance) {
        governance.setCreateTime(LocalDateTime.now());
        governanceMapper.insert(governance);
        return Result.success("添加成功", governance);
    }

    @PutMapping("/update")
    public Result<ComprehensiveGovernance> updateGovernance(@RequestBody ComprehensiveGovernance governance) {
        governanceMapper.updateById(governance);
        return Result.success("更新成功", governance);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteGovernance(@PathVariable Long id) {
        governanceMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/disputes")
    public Result<IPage<ComprehensiveGovernance>> getDisputes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<ComprehensiveGovernance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComprehensiveGovernance::getType, "dispute");
        wrapper.orderByDesc(ComprehensiveGovernance::getCreateTime);
        return Result.success(governanceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/safety")
    public Result<IPage<ComprehensiveGovernance>> getSafety(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<ComprehensiveGovernance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComprehensiveGovernance::getType, "safety");
        wrapper.orderByDesc(ComprehensiveGovernance::getCreateTime);
        return Result.success(governanceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/grid")
    public Result<IPage<ComprehensiveGovernance>> getGrid(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<ComprehensiveGovernance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ComprehensiveGovernance::getType, "grid");
        wrapper.orderByDesc(ComprehensiveGovernance::getCreateTime);
        return Result.success(governanceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/dispute/detail/{id}")
    public Result<ComprehensiveGovernance> getDisputeDetail(@PathVariable Long id) {
        return Result.success(governanceMapper.selectById(id));
    }
}
