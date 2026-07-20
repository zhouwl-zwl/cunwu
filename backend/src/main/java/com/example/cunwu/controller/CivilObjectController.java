
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.CivilObject;
import com.example.cunwu.mapper.CivilObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/civil-object")
public class CivilObjectController {

    private final CivilObjectMapper civilObjectMapper;

    public CivilObjectController(CivilObjectMapper civilObjectMapper) {
        this.civilObjectMapper = civilObjectMapper;
    }

    @GetMapping("/list")
    public Result<IPage<CivilObject>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<CivilObject> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(CivilObject::getName, keyword)
                    .or().like(CivilObject::getIdCard, keyword)
                    .or().like(CivilObject::getPhone, keyword)
                    .or().like(CivilObject::getObjectType, keyword)
                    .or().like(CivilObject::getAddress, keyword));
        }
        wrapper.orderByDesc(CivilObject::getCreateTime);
        return Result.success(civilObjectMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<CivilObject> getById(@PathVariable Long id) {
        return Result.success(civilObjectMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<CivilObject> add(@RequestBody CivilObject civilObject) {
        civilObject.setCreateTime(LocalDateTime.now());
        civilObjectMapper.insert(civilObject);
        return Result.success("新增成功", civilObject);
    }

    @PutMapping("/update")
    public Result<CivilObject> update(@RequestBody CivilObject civilObject) {
        civilObjectMapper.updateById(civilObject);
        return Result.success("更新成功", civilObject);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        civilObjectMapper.deleteById(id);
        return Result.success();
    }
}
