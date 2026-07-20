
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Demand;
import com.example.cunwu.mapper.DemandMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/demand")
public class DemandController {

    private final DemandMapper demandMapper;

    public DemandController(DemandMapper demandMapper) {
        this.demandMapper = demandMapper;
    }

    @PostMapping("/submit")
    public Result<Demand> submitDemand(@RequestBody Demand demand) {
        demand.setStatus("PENDING");
        demand.setCreateTime(LocalDateTime.now());
        demandMapper.insert(demand);
        return Result.success("提交成功", demand);
    }

    @GetMapping("/my-demands")
    public Result<IPage<Demand>> getMyDemands(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Demand::getUserId, userId);
        }
        wrapper.orderByDesc(Demand::getCreateTime);
        
        return Result.success(demandMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/list")
    public Result<IPage<Demand>> getDemandList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Demand::getStatus, status);
        }
        wrapper.orderByDesc(Demand::getCreateTime);
        
        return Result.success(demandMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PutMapping("/handle")
    public Result<Demand> handleDemand(@RequestBody Demand demand) {
        Demand existing = demandMapper.selectById(demand.getId());
        if (existing != null) {
            existing.setHandlerId(demand.getHandlerId());
            existing.setHandlerName(demand.getHandlerName());
            existing.setSolution(demand.getSolution());
            existing.setResultImages(demand.getResultImages());
            existing.setStatus(demand.getStatus());
            existing.setUpdateTime(LocalDateTime.now());
            demandMapper.updateById(existing);
        }
        return Result.success("处理成功", existing);
    }

    @GetMapping("/{id}")
    public Result<Demand> getDemandById(@PathVariable Long id) {
        return Result.success(demandMapper.selectById(id));
    }

    @PutMapping("/rating/{id}")
    public Result<Demand> rateDemand(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Demand existing = demandMapper.selectById(id);
        if (existing != null) {
            demandMapper.updateById(existing);
        }
        return Result.success("评价成功", existing);
    }
}
