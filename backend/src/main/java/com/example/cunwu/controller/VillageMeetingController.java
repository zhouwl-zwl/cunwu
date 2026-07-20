
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.VillageMeeting;
import com.example.cunwu.mapper.VillageMeetingMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/village-meeting")
public class VillageMeetingController {

    private final VillageMeetingMapper villageMeetingMapper;

    public VillageMeetingController(VillageMeetingMapper villageMeetingMapper) {
        this.villageMeetingMapper = villageMeetingMapper;
    }

    @GetMapping("/list")
    public Result<IPage<VillageMeeting>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<VillageMeeting> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(VillageMeeting::getMeetingType, type);
        }
        wrapper.orderByDesc(VillageMeeting::getCreateTime);
        return Result.success(villageMeetingMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<VillageMeeting> getById(@PathVariable Long id) {
        return Result.success(villageMeetingMapper.selectById(id));
    }

    @PostMapping
    public Result<VillageMeeting> add(@RequestBody VillageMeeting villageMeeting) {
        villageMeeting.setCreateTime(LocalDateTime.now());
        villageMeetingMapper.insert(villageMeeting);
        return Result.success("添加成功", villageMeeting);
    }

    @PutMapping
    public Result<VillageMeeting> update(@RequestBody VillageMeeting villageMeeting) {
        villageMeetingMapper.updateById(villageMeeting);
        return Result.success("更新成功", villageMeeting);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        villageMeetingMapper.deleteById(id);
        return Result.success();
    }
}
