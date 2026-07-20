
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("convenience_service")
public class ConvenienceService {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("service_type")
    private String serviceType;

    @TableField("applicant_name")
    private String applicantName;

    @TableField("applicant_phone")
    private String applicantPhone;

    private String content;

    private String status;

    private String handler;

    private String result;

    @TableField("create_time")
    private LocalDateTime createTime;
}
