
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.PartyBuilding;
import com.example.cunwu.mapper.PartyBuildingMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/party-building")
public class PartyBuildingController {

    private final PartyBuildingMapper partyBuildingMapper;

    public PartyBuildingController(PartyBuildingMapper partyBuildingMapper) {
        this.partyBuildingMapper = partyBuildingMapper;
    }

    @GetMapping("/list")
    public Result<IPage<PartyBuilding>> getPartyBuildings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<PartyBuilding> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(PartyBuilding::getType, type);
        }
        wrapper.orderByDesc(PartyBuilding::getCreateTime);
        
        return Result.success(partyBuildingMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<PartyBuilding> getPartyBuildingById(@PathVariable Long id) {
        return Result.success(partyBuildingMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<PartyBuilding> addPartyBuilding(@RequestBody PartyBuilding partyBuilding) {
        partyBuilding.setCreateTime(LocalDateTime.now());
        partyBuildingMapper.insert(partyBuilding);
        return Result.success("添加成功", partyBuilding);
    }

    @PutMapping("/update")
    public Result<PartyBuilding> updatePartyBuilding(@RequestBody PartyBuilding partyBuilding) {
        partyBuildingMapper.updateById(partyBuilding);
        return Result.success("更新成功", partyBuilding);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePartyBuilding(@PathVariable Long id) {
        partyBuildingMapper.deleteById(id);
        return Result.success();
    }
}
