
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sewage_treatment")
public class SewageTreatment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String location;

    @TableField("facility_name")
    private String facilityName;

    private String content;

    private String status;

    private String images;

    @TableField("create_time")
    private LocalDateTime createTime;
}
