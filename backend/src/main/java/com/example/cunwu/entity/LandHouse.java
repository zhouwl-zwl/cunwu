
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("land_house")
public class LandHouse {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("household_name")
    private String householdName;

    @TableField("land_type")
    private String landType;

    private String location;

    private BigDecimal area;

    @TableField("build_date")
    private LocalDateTime buildDate;

    @TableField("owner_name")
    private String ownerName;

    @TableField("owner_id_card")
    private String ownerIdCard;

    private String status;

    @TableField("contract_no")
    private String contractNo;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
