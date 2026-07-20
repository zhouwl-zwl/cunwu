
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("project")
public class Project {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("project_name")
    private String projectName;

    @TableField("project_type")
    private String projectType;

    private String content;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    private String contractor;

    private String supervisor;

    @TableField("start_date")
    private LocalDateTime startDate;

    @TableField("end_date")
    private LocalDateTime endDate;

    private Integer progress;

    private String status;

    private String images;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
