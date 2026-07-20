
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resource")
public class Resource {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;

    private Double area;

    private String location;

    private String status;

    private String contractNo;

    @TableField("contract_date")
    private LocalDateTime contractDate;

    @TableField("expire_date")
    private LocalDateTime expireDate;

    private String lessee;

    private Double rent;

    private String description;

    @TableField("create_time")
    private LocalDateTime createTime;
}
