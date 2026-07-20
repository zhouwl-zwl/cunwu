
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Industry;
import com.example.cunwu.mapper.IndustryMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/industry")
public class IndustryController {

    private final IndustryMapper industryMapper;

    public IndustryController(IndustryMapper industryMapper) {
        this.industryMapper = industryMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Industry>> getIndustries(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Industry> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Industry::getType, type);
        }
        wrapper.orderByDesc(Industry::getCreateTime);
        
        return Result.success(industryMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Industry> getIndustryById(@PathVariable Long id) {
        return Result.success(industryMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Industry> addIndustry(@RequestBody Industry industry) {
        industry.setCreateTime(LocalDateTime.now());
        industryMapper.insert(industry);
        return Result.success("添加成功", industry);
    }

    @PutMapping("/update")
    public Result<Industry> updateIndustry(@RequestBody Industry industry) {
        industryMapper.updateById(industry);
        return Result.success("更新成功", industry);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteIndustry(@PathVariable Long id) {
        industryMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result<Industry> getIndustryDetail(@PathVariable Long id) {
        return Result.success(industryMapper.selectById(id));
    }
}
