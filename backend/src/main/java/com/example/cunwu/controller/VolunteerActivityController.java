
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.VolunteerActivity;
import com.example.cunwu.mapper.VolunteerActivityMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/volunteer-activity")
public class VolunteerActivityController {

    private final VolunteerActivityMapper volunteerActivityMapper;

    public VolunteerActivityController(VolunteerActivityMapper volunteerActivityMapper) {
        this.volunteerActivityMapper = volunteerActivityMapper;
    }

    @GetMapping("/list")
    public Result<IPage<VolunteerActivity>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<VolunteerActivity> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(VolunteerActivity::getTitle, keyword)
                    .or().like(VolunteerActivity::getActivityType, keyword)
                    .or().like(VolunteerActivity::getContent, keyword)
                    .or().like(VolunteerActivity::getLocation, keyword));
        }
        wrapper.orderByDesc(VolunteerActivity::getCreateTime);
        return Result.success(volunteerActivityMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<VolunteerActivity> getById(@PathVariable Long id) {
        return Result.success(volunteerActivityMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<VolunteerActivity> add(@RequestBody VolunteerActivity volunteerActivity) {
        volunteerActivity.setCreateTime(LocalDateTime.now());
        volunteerActivityMapper.insert(volunteerActivity);
        return Result.success("新增成功", volunteerActivity);
    }

    @PutMapping("/update")
    public Result<VolunteerActivity> update(@RequestBody VolunteerActivity volunteerActivity) {
        volunteerActivityMapper.updateById(volunteerActivity);
        return Result.success("更新成功", volunteerActivity);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        volunteerActivityMapper.deleteById(id);
        return Result.success();
    }
}
