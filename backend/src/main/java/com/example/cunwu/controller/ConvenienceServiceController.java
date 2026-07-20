
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.ConvenienceService;
import com.example.cunwu.mapper.ConvenienceServiceMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/convenience-service")
public class ConvenienceServiceController {

    private final ConvenienceServiceMapper convenienceServiceMapper;

    public ConvenienceServiceController(ConvenienceServiceMapper convenienceServiceMapper) {
        this.convenienceServiceMapper = convenienceServiceMapper;
    }

    @GetMapping("/list")
    public Result<IPage<ConvenienceService>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<ConvenienceService> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ConvenienceService::getServiceType, type);
        }
        wrapper.orderByDesc(ConvenienceService::getCreateTime);
        return Result.success(convenienceServiceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<ConvenienceService> getById(@PathVariable Long id) {
        return Result.success(convenienceServiceMapper.selectById(id));
    }

    @PostMapping
    public Result<ConvenienceService> add(@RequestBody ConvenienceService convenienceService) {
        convenienceService.setCreateTime(LocalDateTime.now());
        convenienceServiceMapper.insert(convenienceService);
        return Result.success("添加成功", convenienceService);
    }

    @PutMapping
    public Result<ConvenienceService> update(@RequestBody ConvenienceService convenienceService) {
        convenienceServiceMapper.updateById(convenienceService);
        return Result.success("更新成功", convenienceService);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        convenienceServiceMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/stat")
    public Result<Map<String, Object>> getStat() {
        Map<String, Object> data = new HashMap<>();
        data.put("total", 50);
        data.put("completed", 45);
        data.put("inProgress", 5);
        return Result.success(data);
    }
}
