
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("volunteer")
public class Volunteer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String gender;

    private String phone;

    private String address;

    @TableField("service_type")
    private String serviceType;

    @TableField("join_date")
    private LocalDateTime joinDate;

    @TableField("total_hours")
    private Integer totalHours;

    private Integer points;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
