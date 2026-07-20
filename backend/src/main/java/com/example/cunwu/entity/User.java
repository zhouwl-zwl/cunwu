
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String realName;

    private String avatar;

    private String role;

    private Integer status;

    private Integer auditStatus;

    private String address;

    private String groupName;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    private String village;

    private String isPartyMember;

    private String partyType;

    @TableField("join_party_date")
    private LocalDateTime joinPartyDate;

    private String partyBranch;

    private Integer partyScore;

    private String duty;

    private String manageBusiness;

    @TableField("start_work_date")
    private LocalDateTime startWorkDate;

    private String gridArea;

    private String idCard;

    @TableField("emergency_name")
    private String emergencyName;

    @TableField("emergency_phone")
    private String emergencyPhone;

    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
}
