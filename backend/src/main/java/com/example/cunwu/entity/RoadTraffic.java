
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("road_traffic")
public class RoadTraffic {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("record_type")
    private String recordType;

    private String location;

    private String content;

    private String images;

    private String handler;

    @TableField("create_time")
    private LocalDateTime createTime;
}
