
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.TaskSupervision;
import com.example.cunwu.mapper.TaskSupervisionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/task-supervision")
public class TaskSupervisionController {

    private final TaskSupervisionMapper taskSupervisionMapper;

    public TaskSupervisionController(TaskSupervisionMapper taskSupervisionMapper) {
        this.taskSupervisionMapper = taskSupervisionMapper;
    }

    @GetMapping("/list")
    public Result<IPage<TaskSupervision>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<TaskSupervision> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(TaskSupervision::getSource, type);
        }
        wrapper.orderByDesc(TaskSupervision::getCreateTime);
        return Result.success(taskSupervisionMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<TaskSupervision> getById(@PathVariable Long id) {
        return Result.success(taskSupervisionMapper.selectById(id));
    }

    @PostMapping
    public Result<TaskSupervision> add(@RequestBody TaskSupervision taskSupervision) {
        taskSupervision.setCreateTime(LocalDateTime.now());
        taskSupervisionMapper.insert(taskSupervision);
        return Result.success("添加成功", taskSupervision);
    }

    @PutMapping
    public Result<TaskSupervision> update(@RequestBody TaskSupervision taskSupervision) {
        taskSupervisionMapper.updateById(taskSupervision);
        return Result.success("更新成功", taskSupervision);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskSupervisionMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/stat")
    public Result<Map<String, Object>> getStat() {
        Map<String, Object> data = new HashMap<>();
        data.put("total", taskSupervisionMapper.selectCount(null));
        data.put("completed", taskSupervisionMapper.selectCount(
                new LambdaQueryWrapper<TaskSupervision>().eq(TaskSupervision::getStatus, "completed")));
        data.put("pending", taskSupervisionMapper.selectCount(
                new LambdaQueryWrapper<TaskSupervision>().eq(TaskSupervision::getStatus, "pending")));
        return Result.success(data);
    }
}
