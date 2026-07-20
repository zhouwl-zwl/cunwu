
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.OperationLog;
import com.example.cunwu.mapper.OperationLogMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/operation-log")
public class OperationLogController {

    private final OperationLogMapper operationLogMapper;

    public OperationLogController(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @GetMapping("/list")
    public Result<IPage<OperationLog>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(OperationLog::getOperationType, type);
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return Result.success(operationLogMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<OperationLog> getById(@PathVariable Long id) {
        return Result.success(operationLogMapper.selectById(id));
    }

    @PostMapping
    public Result<OperationLog> add(@RequestBody OperationLog operationLog) {
        operationLog.setCreateTime(LocalDateTime.now());
        operationLogMapper.insert(operationLog);
        return Result.success("添加成功", operationLog);
    }

    @PutMapping
    public Result<OperationLog> update(@RequestBody OperationLog operationLog) {
        operationLogMapper.updateById(operationLog);
        return Result.success("更新成功", operationLog);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        operationLogMapper.deleteById(id);
        return Result.success();
    }
}
