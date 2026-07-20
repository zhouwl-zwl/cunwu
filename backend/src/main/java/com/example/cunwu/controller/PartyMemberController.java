
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.PartyMember;
import com.example.cunwu.mapper.PartyMemberMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/party-member")
public class PartyMemberController {

    private final PartyMemberMapper partyMemberMapper;

    public PartyMemberController(PartyMemberMapper partyMemberMapper) {
        this.partyMemberMapper = partyMemberMapper;
    }

    @GetMapping("/list")
    public Result<IPage<PartyMember>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<PartyMember> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(PartyMember::getPartyType, type);
        }
        wrapper.orderByDesc(PartyMember::getCreateTime);
        return Result.success(partyMemberMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<PartyMember> getById(@PathVariable Long id) {
        return Result.success(partyMemberMapper.selectById(id));
    }

    @PostMapping
    public Result<PartyMember> add(@RequestBody PartyMember partyMember) {
        partyMember.setCreateTime(LocalDateTime.now());
        partyMemberMapper.insert(partyMember);
        return Result.success("添加成功", partyMember);
    }

    @PutMapping
    public Result<PartyMember> update(@RequestBody PartyMember partyMember) {
        partyMemberMapper.updateById(partyMember);
        return Result.success("更新成功", partyMember);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        partyMemberMapper.deleteById(id);
        return Result.success();
    }
}
