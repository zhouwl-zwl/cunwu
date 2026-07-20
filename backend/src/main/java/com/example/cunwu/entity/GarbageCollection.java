
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("garbage_collection")
public class GarbageCollection {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("cleaner_name")
    private String cleanerName;

    private String area;

    private String frequency;

    @TableField("check_date")
    private LocalDateTime checkDate;

    private String notes;

    @TableField("create_time")
    private LocalDateTime createTime;
}
