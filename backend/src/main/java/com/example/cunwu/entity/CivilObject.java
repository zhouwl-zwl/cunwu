
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("civil_object")
public class CivilObject {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("id_card")
    private String idCard;

    private String phone;

    @TableField("object_type")
    private String objectType;

    @TableField("family_members")
    private Integer familyMembers;

    private String address;

    @TableField("subsidy_amount")
    private BigDecimal subsidyAmount;

    private String status;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
