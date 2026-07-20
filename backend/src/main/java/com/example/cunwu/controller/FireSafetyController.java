
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.FireSafety;
import com.example.cunwu.mapper.FireSafetyMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/fire-safety")
public class FireSafetyController {

    private final FireSafetyMapper fireSafetyMapper;

    public FireSafetyController(FireSafetyMapper fireSafetyMapper) {
        this.fireSafetyMapper = fireSafetyMapper;
    }

    @GetMapping("/list")
    public Result<IPage<FireSafety>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<FireSafety> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FireSafety::getCreateTime);
        return Result.success(fireSafetyMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<FireSafety> getById(@PathVariable Long id) {
        return Result.success(fireSafetyMapper.selectById(id));
    }

    @PostMapping
    public Result<FireSafety> add(@RequestBody FireSafety fireSafety) {
        fireSafety.setCreateTime(LocalDateTime.now());
        fireSafetyMapper.insert(fireSafety);
        return Result.success("添加成功", fireSafety);
    }

    @PutMapping
    public Result<FireSafety> update(@RequestBody FireSafety fireSafety) {
        fireSafetyMapper.updateById(fireSafety);
        return Result.success("更新成功", fireSafety);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fireSafetyMapper.deleteById(id);
        return Result.success();
    }
}
