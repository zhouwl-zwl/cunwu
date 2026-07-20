
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dispute")
public class Dispute {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("dispute_type")
    private String disputeType;

    private String parties;

    private String description;

    private String images;

    private String handler;

    @TableField("mediation_record")
    private String mediationRecord;

    private String result;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
