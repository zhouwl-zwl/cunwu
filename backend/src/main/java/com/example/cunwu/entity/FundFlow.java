
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("fund_flow")
public class FundFlow {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;

    private Double amount;

    private String description;

    private String category;

    @TableField("flow_date")
    private LocalDateTime flowDate;

    @TableField("create_time")
    private LocalDateTime createTime;

    private String status;

    private String submitter;

    private String submitterPhone;

    private String files;

    private String auditor;

    private String auditOpinion;

    private LocalDateTime auditTime;

    private String reviewer;

    private String reviewOpinion;

    private LocalDateTime reviewTime;
}
