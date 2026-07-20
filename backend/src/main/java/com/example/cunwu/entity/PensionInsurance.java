
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pension_insurance")
public class PensionInsurance {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("person_name")
    private String personName;

    @TableField("id_card")
    private String idCard;

    private String phone;

    @TableField("insurance_type")
    private String insuranceType;

    private String status;

    @TableField("last_verify_date")
    private LocalDateTime lastVerifyDate;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
