
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Petition;
import com.example.cunwu.mapper.PetitionMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/petition")
public class PetitionController {

    private final PetitionMapper petitionMapper;

    public PetitionController(PetitionMapper petitionMapper) {
        this.petitionMapper = petitionMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Petition>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Petition> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Petition::getPetitionType, keyword)
                    .or().like(Petition::getPetitionerName, keyword)
                    .or().like(Petition::getPetitionerPhone, keyword)
                    .or().like(Petition::getParties, keyword)
                    .or().like(Petition::getDescription, keyword)
                    .or().like(Petition::getHandler, keyword));
        }
        wrapper.orderByDesc(Petition::getCreateTime);
        return Result.success(petitionMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Petition> getById(@PathVariable Long id) {
        return Result.success(petitionMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Petition> add(@RequestBody Petition petition) {
        petition.setCreateTime(LocalDateTime.now());
        petitionMapper.insert(petition);
        return Result.success("新增成功", petition);
    }

    @PutMapping("/update")
    public Result<Petition> update(@RequestBody Petition petition) {
        petitionMapper.updateById(petition);
        return Result.success("更新成功", petition);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        petitionMapper.deleteById(id);
        return Result.success();
    }
}
