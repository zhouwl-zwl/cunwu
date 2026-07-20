
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warehouse_flow")
public class WarehouseFlow {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("item_id")
    private Long itemId;

    @TableField("item_name")
    private String itemName;

    @TableField("flow_type")
    private String flowType;

    private Integer quantity;

    private String applicant;

    private String purpose;

    @TableField("flow_date")
    private LocalDateTime flowDate;

    @TableField("return_date")
    private LocalDateTime returnDate;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
