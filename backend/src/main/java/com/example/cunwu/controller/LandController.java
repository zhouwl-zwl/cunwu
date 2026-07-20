package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.LandHouse;
import com.example.cunwu.mapper.LandHouseMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/land")
public class LandController {

    private final LandHouseMapper landHouseMapper;

    public LandController(LandHouseMapper landHouseMapper) {
        this.landHouseMapper = landHouseMapper;
    }

    @GetMapping("/transfer")
    public Result<IPage<LandHouse>> getTransfer(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<LandHouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(LandHouse::getCreateTime);
        return Result.success(landHouseMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/violation")
    public Result<IPage<LandHouse>> getViolation(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<LandHouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(LandHouse::getCreateTime);
        return Result.success(landHouseMapper.selectPage(new Page<>(page, size), wrapper));
    }
}