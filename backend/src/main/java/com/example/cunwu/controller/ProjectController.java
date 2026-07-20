
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Project;
import com.example.cunwu.mapper.ProjectMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectMapper projectMapper;

    public ProjectController(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Project>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Project::getProjectName, keyword)
                    .or().like(Project::getProjectType, keyword)
                    .or().like(Project::getContent, keyword)
                    .or().like(Project::getContractor, keyword)
                    .or().like(Project::getSupervisor, keyword));
        }
        wrapper.orderByDesc(Project::getCreateTime);
        return Result.success(projectMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Project> getById(@PathVariable Long id) {
        return Result.success(projectMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Project> add(@RequestBody Project project) {
        project.setCreateTime(LocalDateTime.now());
        projectMapper.insert(project);
        return Result.success("新增成功", project);
    }

    @PutMapping("/update")
    public Result<Project> update(@RequestBody Project project) {
        projectMapper.updateById(project);
        return Result.success("更新成功", project);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        projectMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/ongoing")
    public Result<IPage<Project>> getOngoing(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getStatus, "ongoing");
        wrapper.orderByDesc(Project::getCreateTime);
        return Result.success(projectMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/completed")
    public Result<IPage<Project>> getCompleted(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getStatus, "completed");
        wrapper.orderByDesc(Project::getCreateTime);
        return Result.success(projectMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/contract")
    public Result<IPage<Project>> getContract(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Project::getContractor);
        wrapper.orderByDesc(Project::getCreateTime);
        return Result.success(projectMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/payment")
    public Result<IPage<Project>> getPayment(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Project::getTotalAmount);
        wrapper.orderByDesc(Project::getCreateTime);
        return Result.success(projectMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
