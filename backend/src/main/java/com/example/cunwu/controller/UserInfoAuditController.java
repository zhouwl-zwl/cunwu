package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Notification;
import com.example.cunwu.entity.User;
import com.example.cunwu.entity.UserInfoAudit;
import com.example.cunwu.mapper.NotificationMapper;
import com.example.cunwu.mapper.UserInfoAuditMapper;
import com.example.cunwu.mapper.UserMapper;
import com.example.cunwu.security.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
public class UserInfoAuditController {

    private final UserInfoAuditMapper userInfoAuditMapper;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public UserInfoAuditController(UserInfoAuditMapper userInfoAuditMapper, UserMapper userMapper,
                                   NotificationMapper notificationMapper, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.userInfoAuditMapper = userInfoAuditMapper;
        this.userMapper = userMapper;
        this.notificationMapper = notificationMapper;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/list")
    public Result<IPage<UserInfoAudit>> getAuditList(
            @RequestParam(required = false) Long villageId,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<UserInfoAudit> page = new Page<>(current, size);
        QueryWrapper<UserInfoAudit> queryWrapper = new QueryWrapper<>();

        if (villageId != null) {
            queryWrapper.eq("village_id", villageId);
        }
        if (auditStatus != null) {
            queryWrapper.eq("audit_status", auditStatus);
        }
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("village_name", name);
        }

        queryWrapper.orderByDesc("audit_status");
        queryWrapper.orderByDesc("submit_time");

        return Result.success(userInfoAuditMapper.selectPage(page, queryWrapper));
    }

    @GetMapping("/user-list")
    public Result<List<UserInfoAudit>> getUserAuditList(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        QueryWrapper<UserInfoAudit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("submit_time");
        return Result.success(userInfoAuditMapper.selectList(queryWrapper));
    }

    @GetMapping("/detail/{auditId}")
    public Result<UserInfoAudit> getAuditDetail(@PathVariable Long auditId) {
        UserInfoAudit audit = userInfoAuditMapper.selectById(auditId);
        if (audit == null) {
            return Result.error(404, "审核单不存在");
        }
        return Result.success(audit);
    }

    @PostMapping("/pass")
    public Result<String> auditPass(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long adminId = jwtUtil.getUserIdFromToken(token);

        Long auditId = ((Number) params.get("auditId")).longValue();
        String adminName = (String) params.get("adminName");

        UserInfoAudit audit = userInfoAuditMapper.selectById(auditId);
        if (audit == null || audit.getAuditStatus() != 0) {
            return Result.error(400, "审核单不存在或已处理");
        }

        try {
            Map<String, Object> newDataMap = objectMapper.readValue(audit.getNewData(), new TypeReference<Map<String, Object>>() {});
            User user = userMapper.selectById(audit.getUserId());

            if (newDataMap.containsKey("isPartyMember")) user.setIsPartyMember((String) newDataMap.get("isPartyMember"));
            if (newDataMap.containsKey("partyType")) user.setPartyType((String) newDataMap.get("partyType"));
            if (newDataMap.containsKey("joinPartyDate")) {
                String dateStr = (String) newDataMap.get("joinPartyDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    user.setJoinPartyDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                }
            }
            if (newDataMap.containsKey("partyBranch")) user.setPartyBranch((String) newDataMap.get("partyBranch"));
            if (newDataMap.containsKey("duty")) user.setDuty((String) newDataMap.get("duty"));
            if (newDataMap.containsKey("manageBusiness")) user.setManageBusiness((String) newDataMap.get("manageBusiness"));
            if (newDataMap.containsKey("startWorkDate")) {
                String dateStr = (String) newDataMap.get("startWorkDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    user.setStartWorkDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                }
            }
            if (newDataMap.containsKey("gridArea")) user.setGridArea((String) newDataMap.get("gridArea"));

            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);

            audit.setAuditStatus(1);
            audit.setAuditAdminId(adminId);
            audit.setAuditAdminName(adminName);
            audit.setAuditTime(LocalDateTime.now());
            userInfoAuditMapper.updateById(audit);

            createAuditNotification(audit.getUserId(), "信息审核通知", "您的职务/党员信息已审核通过，点击查看最新资料", "audit");
            return Result.success("审核通过");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Result.error(500, "审核失败");
        }
    }

    @PostMapping("/reject")
    public Result<String> auditReject(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long adminId = jwtUtil.getUserIdFromToken(token);

        Long auditId = ((Number) params.get("auditId")).longValue();
        String adminName = (String) params.get("adminName");
        String opinion = (String) params.get("opinion");

        UserInfoAudit audit = userInfoAuditMapper.selectById(auditId);
        if (audit == null || audit.getAuditStatus() != 0) {
            return Result.error(400, "审核单不存在或已处理");
        }

        audit.setAuditStatus(2);
        audit.setAuditAdminId(adminId);
        audit.setAuditAdminName(adminName);
        audit.setAuditOpinion(opinion);
        audit.setAuditTime(LocalDateTime.now());
        userInfoAuditMapper.updateById(audit);

        createAuditNotification(audit.getUserId(), "信息审核通知", "您的信息变更申请已驳回，驳回原因：" + opinion + "，点击重新修改", "audit");
        return Result.success("已驳回");
    }

    @PostMapping("/batch-pass")
    public Result<String> batchAuditPass(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long adminId = jwtUtil.getUserIdFromToken(token);

        @SuppressWarnings("unchecked")
        List<Long> auditIds = (List<Long>) params.get("auditIds");
        String adminName = (String) params.get("adminName");

        for (Long auditId : auditIds) {
            UserInfoAudit audit = userInfoAuditMapper.selectById(auditId);
            if (audit != null && audit.getAuditStatus() == 0) {
                try {
                    Map<String, Object> newDataMap = objectMapper.readValue(audit.getNewData(), new TypeReference<Map<String, Object>>() {});
                    User user = userMapper.selectById(audit.getUserId());

                    if (newDataMap.containsKey("isPartyMember")) user.setIsPartyMember((String) newDataMap.get("isPartyMember"));
                    if (newDataMap.containsKey("partyType")) user.setPartyType((String) newDataMap.get("partyType"));
                    if (newDataMap.containsKey("joinPartyDate")) {
                        String dateStr = (String) newDataMap.get("joinPartyDate");
                        if (dateStr != null && !dateStr.isEmpty()) {
                            user.setJoinPartyDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                        }
                    }
                    if (newDataMap.containsKey("partyBranch")) user.setPartyBranch((String) newDataMap.get("partyBranch"));
                    if (newDataMap.containsKey("duty")) user.setDuty((String) newDataMap.get("duty"));
                    if (newDataMap.containsKey("manageBusiness")) user.setManageBusiness((String) newDataMap.get("manageBusiness"));
                    if (newDataMap.containsKey("startWorkDate")) {
                        String dateStr = (String) newDataMap.get("startWorkDate");
                        if (dateStr != null && !dateStr.isEmpty()) {
                            user.setStartWorkDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                        }
                    }
                    if (newDataMap.containsKey("gridArea")) user.setGridArea((String) newDataMap.get("gridArea"));

                    user.setUpdateTime(LocalDateTime.now());
                    userMapper.updateById(user);

                    audit.setAuditStatus(1);
                    audit.setAuditAdminId(adminId);
                    audit.setAuditAdminName(adminName);
                    audit.setAuditTime(LocalDateTime.now());
                    userInfoAuditMapper.updateById(audit);

                    createAuditNotification(audit.getUserId(), "信息审核通知", "您的职务/党员信息已审核通过，点击查看最新资料", "audit");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success("批量审核通过");
    }

    @PostMapping("/batch-reject")
    public Result<String> batchAuditReject(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long adminId = jwtUtil.getUserIdFromToken(token);

        @SuppressWarnings("unchecked")
        List<Long> auditIds = (List<Long>) params.get("auditIds");
        String adminName = (String) params.get("adminName");
        String opinion = (String) params.get("opinion");

        for (Long auditId : auditIds) {
            UserInfoAudit audit = userInfoAuditMapper.selectById(auditId);
            if (audit != null && audit.getAuditStatus() == 0) {
                audit.setAuditStatus(2);
                audit.setAuditAdminId(adminId);
                audit.setAuditAdminName(adminName);
                audit.setAuditOpinion(opinion);
                audit.setAuditTime(LocalDateTime.now());
                userInfoAuditMapper.updateById(audit);

                createAuditNotification(audit.getUserId(), "信息审核通知", "您的信息变更申请已驳回，驳回原因：" + opinion + "，点击重新修改", "audit");
            }
        }
        return Result.success("批量驳回完成");
    }

    @GetMapping("/pending-check")
    public Result<Map<String, Boolean>> checkPendingAudit(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        long pendingCount = userInfoAuditMapper.selectCount(
            new LambdaQueryWrapper<UserInfoAudit>()
                .eq(UserInfoAudit::getUserId, userId)
                .eq(UserInfoAudit::getAuditStatus, 0)
        );
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasPending", pendingCount > 0);
        return Result.success(result);
    }

    private void createAuditNotification(Long userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setStatus(1);
        notification.setIsTop(0);
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);
    }
}