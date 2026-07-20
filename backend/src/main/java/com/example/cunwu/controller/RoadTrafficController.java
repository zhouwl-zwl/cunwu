
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.RoadTraffic;
import com.example.cunwu.mapper.RoadTrafficMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/road-traffic")
public class RoadTrafficController {

    private final RoadTrafficMapper roadTrafficMapper;

    public RoadTrafficController(RoadTrafficMapper roadTrafficMapper) {
        this.roadTrafficMapper = roadTrafficMapper;
    }

    @GetMapping("/list")
    public Result<IPage<RoadTraffic>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<RoadTraffic> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(RoadTraffic::getRecordType, type);
        }
        wrapper.orderByDesc(RoadTraffic::getCreateTime);
        return Result.success(roadTrafficMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<RoadTraffic> getById(@PathVariable Long id) {
        return Result.success(roadTrafficMapper.selectById(id));
    }

    @PostMapping
    public Result<RoadTraffic> add(@RequestBody RoadTraffic roadTraffic) {
        roadTraffic.setCreateTime(LocalDateTime.now());
        roadTrafficMapper.insert(roadTraffic);
        return Result.success("添加成功", roadTraffic);
    }

    @PutMapping
    public Result<RoadTraffic> update(@RequestBody RoadTraffic roadTraffic) {
        roadTrafficMapper.updateById(roadTraffic);
        return Result.success("更新成功", roadTraffic);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roadTrafficMapper.deleteById(id);
        return Result.success();
    }
}
