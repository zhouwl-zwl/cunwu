
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("safety_hazard")
public class SafetyHazard {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String location;

    @TableField("hazard_type")
    private String hazardType;

    private String description;

    private String images;

    private LocalDateTime deadline;

    private String status;

    private String handler;

    private String result;

    @TableField("create_time")
    private LocalDateTime createTime;
}
