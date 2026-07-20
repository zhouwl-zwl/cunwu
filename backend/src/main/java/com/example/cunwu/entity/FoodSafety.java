
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("food_safety")
public class FoodSafety {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String location;

    @TableField("check_content")
    private String checkContent;

    private String images;

    private String issues;

    private String handler;

    @TableField("check_date")
    private LocalDateTime checkDate;

    @TableField("create_time")
    private LocalDateTime createTime;
}
