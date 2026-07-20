
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Population;
import com.example.cunwu.mapper.PopulationMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/population")
public class PopulationController {

    private final PopulationMapper populationMapper;

    public PopulationController(PopulationMapper populationMapper) {
        this.populationMapper = populationMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Population>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Population> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Population::getHouseholdName, keyword)
                    .or().like(Population::getIdCard, keyword)
                    .or().like(Population::getPhone, keyword)
                    .or().like(Population::getAddress, keyword)
                    .or().like(Population::getWorkLocation, keyword)
                    .or().like(Population::getGroupName, keyword));
        }
        wrapper.orderByDesc(Population::getCreateTime);
        return Result.success(populationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Population> getById(@PathVariable Long id) {
        return Result.success(populationMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Population> add(@RequestBody Population population) {
        population.setCreateTime(LocalDateTime.now());
        populationMapper.insert(population);
        return Result.success("新增成功", population);
    }

    @PutMapping("/update")
    public Result<Population> update(@RequestBody Population population) {
        populationMapper.updateById(population);
        return Result.success("更新成功", population);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        populationMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/household")
    public Result<IPage<Population>> getHousehold(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Population> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Population::getCreateTime);
        return Result.success(populationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/migrant")
    public Result<IPage<Population>> getMigrant(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Population> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Population::getWorkLocation);
        wrapper.orderByDesc(Population::getCreateTime);
        return Result.success(populationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/change")
    public Result<IPage<Population>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Population> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Population::getCreateTime);
        return Result.success(populationMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
