
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.FoodSafety;
import com.example.cunwu.mapper.FoodSafetyMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/food-safety")
public class FoodSafetyController {

    private final FoodSafetyMapper foodSafetyMapper;

    public FoodSafetyController(FoodSafetyMapper foodSafetyMapper) {
        this.foodSafetyMapper = foodSafetyMapper;
    }

    @GetMapping("/list")
    public Result<IPage<FoodSafety>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<FoodSafety> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FoodSafety::getCreateTime);
        return Result.success(foodSafetyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<FoodSafety> getById(@PathVariable Long id) {
        return Result.success(foodSafetyMapper.selectById(id));
    }

    @PostMapping
    public Result<FoodSafety> add(@RequestBody FoodSafety foodSafety) {
        foodSafety.setCreateTime(LocalDateTime.now());
        foodSafetyMapper.insert(foodSafety);
        return Result.success("添加成功", foodSafety);
    }

    @PutMapping
    public Result<FoodSafety> update(@RequestBody FoodSafety foodSafety) {
        foodSafetyMapper.updateById(foodSafety);
        return Result.success("更新成功", foodSafety);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        foodSafetyMapper.deleteById(id);
        return Result.success();
    }
}
