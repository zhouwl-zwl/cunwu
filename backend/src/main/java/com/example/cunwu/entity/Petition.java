
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("petition")
public class Petition {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("petition_type")
    private String petitionType;

    @TableField("petitioner_name")
    private String petitionerName;

    @TableField("petitioner_phone")
    private String petitionerPhone;

    private String parties;

    private String description;

    private String images;

    private String handler;

    @TableField("mediation_record")
    private String mediationRecord;

    private String agreement;

    private String result;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
