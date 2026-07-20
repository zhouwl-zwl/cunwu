
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("environment_check")
public class EnvironmentCheck {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("check_type")
    private String checkType;

    private String location;

    private String description;

    @TableField("before_images")
    private String beforeImages;

    @TableField("after_images")
    private String afterImages;

    private String inspector;

    @TableField("check_date")
    private LocalDateTime checkDate;

    private String status;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
