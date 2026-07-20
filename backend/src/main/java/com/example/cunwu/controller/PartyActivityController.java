
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.PartyActivity;
import com.example.cunwu.mapper.PartyActivityMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/party-activity")
public class PartyActivityController {

    private final PartyActivityMapper partyActivityMapper;

    public PartyActivityController(PartyActivityMapper partyActivityMapper) {
        this.partyActivityMapper = partyActivityMapper;
    }

    @GetMapping("/list")
    public Result<IPage<PartyActivity>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<PartyActivity> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(PartyActivity::getActivityType, type);
        }
        wrapper.orderByDesc(PartyActivity::getCreateTime);
        return Result.success(partyActivityMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<PartyActivity> getById(@PathVariable Long id) {
        return Result.success(partyActivityMapper.selectById(id));
    }

    @PostMapping
    public Result<PartyActivity> add(@RequestBody PartyActivity partyActivity) {
        partyActivity.setCreateTime(LocalDateTime.now());
        partyActivityMapper.insert(partyActivity);
        return Result.success("添加成功", partyActivity);
    }

    @PutMapping
    public Result<PartyActivity> update(@RequestBody PartyActivity partyActivity) {
        partyActivityMapper.updateById(partyActivity);
        return Result.success("更新成功", partyActivity);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        partyActivityMapper.deleteById(id);
        return Result.success();
    }
}
