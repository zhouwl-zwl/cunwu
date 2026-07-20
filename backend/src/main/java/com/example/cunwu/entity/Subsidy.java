
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("subsidy")
public class Subsidy {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("subsidy_type")
    private String subsidyType;

    @TableField("recipient_name")
    private String recipientName;

    @TableField("recipient_id_card")
    private String recipientIdCard;

    @TableField("recipient_phone")
    private String recipientPhone;

    @TableField("bank_card")
    private String bankCard;

    private BigDecimal amount;

    @TableField("issue_month")
    private String issueMonth;

    private String status;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
