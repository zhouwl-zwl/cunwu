
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.*;
import com.example.cunwu.mapper.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VillageInfoMapper villageInfoMapper;
    private final OrgMemberMapper orgMemberMapper;
    private final DemandMapper demandMapper;
    private final NotificationMapper notificationMapper;

    public AdminController(UserMapper userMapper, PasswordEncoder passwordEncoder,
                           VillageInfoMapper villageInfoMapper, OrgMemberMapper orgMemberMapper,
                           DemandMapper demandMapper, NotificationMapper notificationMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.villageInfoMapper = villageInfoMapper;
        this.orgMemberMapper = orgMemberMapper;
        this.demandMapper = demandMapper;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/users")
    public Result<IPage<User>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer auditStatus) {
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        if (auditStatus != null) {
            wrapper.eq(User::getAuditStatus, auditStatus);
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        return Result.success(userMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PostMapping("/user/add")
    public Result<User> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setAuditStatus(1);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        return Result.success("添加成功", user);
    }

    @PutMapping("/user/update")
    public Result<User> updateUser(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return Result.success("更新成功", user);
    }

    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/user/audit/{id}")
    public Result<Void> auditUser(@PathVariable Long id, @RequestParam Integer auditStatus) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setAuditStatus(auditStatus);
            userMapper.updateById(user);
        }
        return Result.success();
    }

    @GetMapping("/village-info")
    public Result<VillageInfo> getVillageInfo() {
        return Result.success(villageInfoMapper.selectList(null).stream().findFirst().orElse(null));
    }

    @PutMapping("/village-info")
    public Result<VillageInfo> updateVillageInfo(@RequestBody VillageInfo villageInfo) {
        villageInfo.setUpdateTime(LocalDateTime.now());
        villageInfoMapper.updateById(villageInfo);
        return Result.success("更新成功", villageInfo);
    }

    @PostMapping("/org-member/add")
    public Result<OrgMember> addOrgMember(@RequestBody OrgMember orgMember) {
        orgMember.setCreateTime(LocalDateTime.now());
        orgMemberMapper.insert(orgMember);
        return Result.success("添加成功", orgMember);
    }

    @PutMapping("/org-member/update")
    public Result<OrgMember> updateOrgMember(@RequestBody OrgMember orgMember) {
        orgMemberMapper.updateById(orgMember);
        return Result.success("更新成功", orgMember);
    }

    @DeleteMapping("/org-member/{id}")
    public Result<Void> deleteOrgMember(@PathVariable Long id) {
        orgMemberMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("totalUsers", userMapper.selectCount(null));
        data.put("pendingAudit", userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getAuditStatus, 0)));
        data.put("totalDemands", demandMapper.selectCount(null));
        data.put("pendingDemands", demandMapper.selectCount(new LambdaQueryWrapper<Demand>().eq(Demand::getStatus, "PENDING")));
        data.put("completedDemands", demandMapper.selectCount(new LambdaQueryWrapper<Demand>().eq(Demand::getStatus, "COMPLETED")));
        data.put("totalNotifications", notificationMapper.selectCount(null));
        
        VillageInfo villageInfo = villageInfoMapper.selectList(null).stream().findFirst().orElse(null);
        if (villageInfo != null) {
            data.put("population", villageInfo.getPopulation());
            data.put("area", villageInfo.getArea());
            data.put("householdCount", villageInfo.getHouseholdCount());
            data.put("totalAssets", villageInfo.getTotalAssets());
        }
        
        return Result.success(data);
    }
}
