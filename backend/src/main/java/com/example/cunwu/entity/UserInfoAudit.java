package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_info_audit")
public class UserInfoAudit {

    @TableId(type = IdType.AUTO)
    private Long auditId;

    @TableField("user_id")
    private Long userId;

    @TableField("village_id")
    private Long villageId;

    @TableField("village_name")
    private String villageName;

    @TableField("change_type")
    private String changeType;

    @TableField("old_data")
    private String oldData;

    @TableField("new_data")
    private String newData;

    @TableField("audit_status")
    private Integer auditStatus;

    @TableField("audit_admin_id")
    private Long auditAdminId;

    @TableField("audit_admin_name")
    private String auditAdminName;

    @TableField("audit_opinion")
    private String auditOpinion;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("audit_time")
    private LocalDateTime auditTime;
}
