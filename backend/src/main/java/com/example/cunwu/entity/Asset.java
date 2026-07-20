
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("asset")
public class Asset {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;

    @TableField("asset_value")
    private Double value;

    private String location;

    private String status;

    @TableField("buy_date")
    private LocalDateTime buyDate;

    @TableField("depreciation_rate")
    private Double depreciationRate;

    private String description;

    @TableField("create_time")
    private LocalDateTime createTime;
}
