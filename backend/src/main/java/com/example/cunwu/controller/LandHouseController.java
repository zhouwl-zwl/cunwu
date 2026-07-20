
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.LandHouse;
import com.example.cunwu.mapper.LandHouseMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/land-house")
public class LandHouseController {

    private final LandHouseMapper landHouseMapper;

    public LandHouseController(LandHouseMapper landHouseMapper) {
        this.landHouseMapper = landHouseMapper;
    }

    @GetMapping("/list")
    public Result<IPage<LandHouse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<LandHouse> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(LandHouse::getHouseholdName, keyword)
                    .or().like(LandHouse::getLocation, keyword)
                    .or().like(LandHouse::getOwnerName, keyword)
                    .or().like(LandHouse::getOwnerIdCard, keyword)
                    .or().like(LandHouse::getContractNo, keyword));
        }
        wrapper.orderByDesc(LandHouse::getCreateTime);
        return Result.success(landHouseMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<LandHouse> getById(@PathVariable Long id) {
        return Result.success(landHouseMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<LandHouse> add(@RequestBody LandHouse landHouse) {
        landHouse.setCreateTime(LocalDateTime.now());
        landHouseMapper.insert(landHouse);
        return Result.success("新增成功", landHouse);
    }

    @PutMapping("/update")
    public Result<LandHouse> update(@RequestBody LandHouse landHouse) {
        landHouseMapper.updateById(landHouse);
        return Result.success("更新成功", landHouse);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        landHouseMapper.deleteById(id);
        return Result.success();
    }
}
