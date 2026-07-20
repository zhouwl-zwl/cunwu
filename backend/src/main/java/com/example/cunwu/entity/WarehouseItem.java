
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("warehouse_item")
public class WarehouseItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String category;

    private Integer quantity;

    private String unit;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("storage_location")
    private String storageLocation;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
