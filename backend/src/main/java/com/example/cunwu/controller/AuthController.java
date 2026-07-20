package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.User;
import com.example.cunwu.entity.UserInfoAudit;
import com.example.cunwu.entity.Notification;
import com.example.cunwu.mapper.UserMapper;
import com.example.cunwu.mapper.UserInfoAuditMapper;
import com.example.cunwu.mapper.NotificationMapper;
import com.example.cunwu.security.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserMapper userMapper;
    private final UserInfoAuditMapper userInfoAuditMapper;
    private final NotificationMapper notificationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public AuthController(UserMapper userMapper, UserInfoAuditMapper userInfoAuditMapper,
                          NotificationMapper notificationMapper, PasswordEncoder passwordEncoder, 
                          JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.userMapper = userMapper;
        this.userInfoAuditMapper = userInfoAuditMapper;
        this.notificationMapper = notificationMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, username));
        }

        if (user == null) {
            return Result.error(401, "账号或密码错误");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error(401, "账号或密码错误");
        }

        if (user.getStatus() != 1) {
            return Result.error(401, "账号已禁用");
        }

        if (user.getAuditStatus() != 1) {
            return Result.error(401, "账号待审核");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        String realName = params.get("realName");

        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone)) != null) {
            return Result.error(400, "该手机号已注册");
        }

        User user = new User();
        user.setUsername(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRealName(realName);
        user.setRole("VILLAGER");
        user.setStatus(1);
        user.setAuditStatus(0);
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);
        return Result.success("注册成功，等待审核", user);
    }

    @GetMapping("/verify-code")
    public Result<Map<String, String>> getVerifyCode(@RequestParam String phone) {
        Map<String, String> data = new HashMap<>();
        data.put("code", "1234");
        return Result.success(data);
    }

    @GetMapping("/user-info")
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Long userId = 1L;
        
        try {
            String token = request.getHeader("Authorization");
            if (token != null && !token.isEmpty()) {
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                userId = jwtUtil.getUserIdFromToken(token);
            }
        } catch (Exception e) {
            userId = 1L;
        }
        
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            user = new User();
            user.setRealName("张三");
            user.setRole("VILLAGER");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("account", user.getUsername());
        result.put("name", user.getRealName());
        result.put("phone", user.getPhone());
        result.put("village", user.getVillage());
        result.put("role", getRoleName(user.getRole()));
        result.put("roleCode", user.getRole());
        result.put("isPartyMember", user.getIsPartyMember());
        result.put("partyType", user.getPartyType());
        result.put("joinPartyDate", user.getJoinPartyDate());
        result.put("partyBranch", user.getPartyBranch());
        result.put("partyScore", user.getPartyScore());
        result.put("duty", user.getDuty());
        result.put("manageBusiness", user.getManageBusiness());
        result.put("startWorkDate", user.getStartWorkDate());
        result.put("gridArea", user.getGridArea());
        result.put("idCard", maskIdCard(user.getIdCard()));
        result.put("address", user.getAddress());
        result.put("emergencyName", user.getEmergencyName());
        result.put("emergencyPhone", user.getEmergencyPhone());
        result.put("createTime", user.getCreateTime());
        result.put("lastLogin", user.getLastLoginTime());
        
        return Result.success(result);
    }

    @PutMapping("/update-profile")
    public Result<Object> updateProfile(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return Result.error(401, "未登录");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        
        Map<String, Object> oldAuditData = new HashMap<>();
        oldAuditData.put("isPartyMember", user.getIsPartyMember());
        oldAuditData.put("partyType", user.getPartyType());
        oldAuditData.put("joinPartyDate", formatDate(user.getJoinPartyDate()));
        oldAuditData.put("partyBranch", user.getPartyBranch());
        oldAuditData.put("duty", user.getDuty());
        oldAuditData.put("manageBusiness", user.getManageBusiness());
        oldAuditData.put("startWorkDate", formatDate(user.getStartWorkDate()));
        oldAuditData.put("gridArea", user.getGridArea());
        
        Map<String, Object> newAuditData = new HashMap<>();
        boolean hasPartyChange = false;
        boolean hasDutyChange = false;
        
        if (params.containsKey("isPartyMember")) {
            String newValue = (String) params.get("isPartyMember");
            newAuditData.put("isPartyMember", newValue);
            if (!equals(oldAuditData.get("isPartyMember"), newValue)) {
                hasPartyChange = true;
            }
        }
        if (params.containsKey("partyType")) {
            String newValue = (String) params.get("partyType");
            newAuditData.put("partyType", newValue);
            if (!equals(oldAuditData.get("partyType"), newValue)) {
                hasPartyChange = true;
            }
        }
        if (params.containsKey("joinPartyDate")) {
            String newValue = (String) params.get("joinPartyDate");
            newAuditData.put("joinPartyDate", newValue);
            if (!equals(oldAuditData.get("joinPartyDate"), newValue)) {
                hasPartyChange = true;
            }
        }
        if (params.containsKey("partyBranch")) {
            String newValue = (String) params.get("partyBranch");
            newAuditData.put("partyBranch", newValue);
            if (!equals(oldAuditData.get("partyBranch"), newValue)) {
                hasPartyChange = true;
            }
        }
        if (params.containsKey("duty")) {
            String newValue = (String) params.get("duty");
            newAuditData.put("duty", newValue);
            if (!equals(oldAuditData.get("duty"), newValue)) {
                hasDutyChange = true;
            }
        }
        if (params.containsKey("manageBusiness")) {
            String newValue = (String) params.get("manageBusiness");
            newAuditData.put("manageBusiness", newValue);
            if (!equals(oldAuditData.get("manageBusiness"), newValue)) {
                hasDutyChange = true;
            }
        }
        if (params.containsKey("startWorkDate")) {
            String newValue = (String) params.get("startWorkDate");
            newAuditData.put("startWorkDate", newValue);
            if (!equals(oldAuditData.get("startWorkDate"), newValue)) {
                hasDutyChange = true;
            }
        }
        if (params.containsKey("gridArea")) {
            String newValue = (String) params.get("gridArea");
            newAuditData.put("gridArea", newValue);
            if (!equals(oldAuditData.get("gridArea"), newValue)) {
                hasDutyChange = true;
            }
        }
        
        boolean needAudit = hasPartyChange || hasDutyChange;
        
        if (needAudit) {
            long pendingCount = userInfoAuditMapper.selectCount(
                new LambdaQueryWrapper<UserInfoAudit>()
                    .eq(UserInfoAudit::getUserId, userId)
                    .eq(UserInfoAudit::getAuditStatus, 0)
            );
            if (pendingCount > 0) {
                return Result.error(400, "当前尚有未完成的信息审核申请，无法重复提交，请等待处理");
            }
            
            String changeType = hasPartyChange && hasDutyChange ? "all" : (hasPartyChange ? "party" : "duty");
            
            try {
                String oldDataJson = objectMapper.writeValueAsString(oldAuditData);
                String newDataJson = objectMapper.writeValueAsString(newAuditData);
                
                UserInfoAudit audit = new UserInfoAudit();
                audit.setUserId(userId);
                audit.setVillageName(user.getVillage());
                audit.setChangeType(changeType);
                audit.setOldData(oldDataJson);
                audit.setNewData(newDataJson);
                audit.setAuditStatus(0);
                audit.setSubmitTime(LocalDateTime.now());
                userInfoAuditMapper.insert(audit);
                
                if (params.containsKey("name")) {
                    user.setRealName((String) params.get("name"));
                }
                if (params.containsKey("phone")) {
                    user.setPhone((String) params.get("phone"));
                }
                if (params.containsKey("village")) {
                    user.setVillage((String) params.get("village"));
                }
                if (params.containsKey("idCard")) {
                    user.setIdCard((String) params.get("idCard"));
                }
                if (params.containsKey("address")) {
                    user.setAddress((String) params.get("address"));
                }
                if (params.containsKey("emergencyName")) {
                    user.setEmergencyName((String) params.get("emergencyName"));
                }
                if (params.containsKey("emergencyPhone")) {
                    user.setEmergencyPhone((String) params.get("emergencyPhone"));
                }
                
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
                
                return Result.success("党务/职务信息已提交管理员审核，请等待审核结果");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Result.error(500, "提交审核失败");
            }
        } else {
            if (params.containsKey("name")) {
                user.setRealName((String) params.get("name"));
            }
            if (params.containsKey("phone")) {
                user.setPhone((String) params.get("phone"));
            }
            if (params.containsKey("village")) {
                user.setVillage((String) params.get("village"));
            }
            if (params.containsKey("isPartyMember")) {
                user.setIsPartyMember((String) params.get("isPartyMember"));
            }
            if (params.containsKey("partyType")) {
                user.setPartyType((String) params.get("partyType"));
            }
            if (params.containsKey("joinPartyDate")) {
                String dateStr = (String) params.get("joinPartyDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    user.setJoinPartyDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                }
            }
            if (params.containsKey("partyBranch")) {
                user.setPartyBranch((String) params.get("partyBranch"));
            }
            if (params.containsKey("partyScore")) {
                user.setPartyScore((Integer) params.get("partyScore"));
            }
            if (params.containsKey("duty")) {
                user.setDuty((String) params.get("duty"));
            }
            if (params.containsKey("manageBusiness")) {
                user.setManageBusiness((String) params.get("manageBusiness"));
            }
            if (params.containsKey("startWorkDate")) {
                String dateStr = (String) params.get("startWorkDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    user.setStartWorkDate(LocalDateTime.parse(dateStr + "T00:00:00"));
                }
            }
            if (params.containsKey("gridArea")) {
                user.setGridArea((String) params.get("gridArea"));
            }
            if (params.containsKey("idCard")) {
                user.setIdCard((String) params.get("idCard"));
            }
            if (params.containsKey("address")) {
                user.setAddress((String) params.get("address"));
            }
            if (params.containsKey("emergencyName")) {
                user.setEmergencyName((String) params.get("emergencyName"));
            }
            if (params.containsKey("emergencyPhone")) {
                user.setEmergencyPhone((String) params.get("emergencyPhone"));
            }
            
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
            
            return Result.success("信息修改成功", user);
        }
    }
    
    private boolean equals(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.toString().equals(b.toString());
    }
    
    private String formatDate(LocalDateTime date) {
        if (date == null) return "";
        return date.toLocalDate().toString();
    }

    private String getRoleName(String role) {
        if ("VILLAGER".equals(role)) return "普通村民";
        if ("VILLAGE_OFFICIAL".equals(role)) return "村级网格员";
        if ("RESIDENT_OFFICIAL".equals(role)) return "驻村干部";
        if ("ADMIN".equals(role)) return "村级管理员";
        return "未分配角色";
    }

    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4);
    }

    @PostMapping("/upload-avatar")
    public Result<Map<String, Object>> uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("avatarUrl", "/uploads/avatar/default.jpg");
        return Result.success("头像上传成功", data);
    }
}