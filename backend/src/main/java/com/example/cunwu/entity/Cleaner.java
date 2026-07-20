
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("cleaner")
public class Cleaner {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String phone;

    private String area;

    private String attendance;

    private BigDecimal salary;

    @TableField("create_time")
    private LocalDateTime createTime;
}
