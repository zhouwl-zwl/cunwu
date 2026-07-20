package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.CivilObject;
import com.example.cunwu.mapper.CivilObjectMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/civil")
public class CivilController {

    private final CivilObjectMapper civilObjectMapper;

    public CivilController(CivilObjectMapper civilObjectMapper) {
        this.civilObjectMapper = civilObjectMapper;
    }

    @GetMapping("/low-income")
    public Result<IPage<CivilObject>> getLowIncome(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<CivilObject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CivilObject::getObjectType, "low-income");
        wrapper.orderByDesc(CivilObject::getCreateTime);
        return Result.success(civilObjectMapper.selectPage(new Page<>(page, size), wrapper));
    }
}