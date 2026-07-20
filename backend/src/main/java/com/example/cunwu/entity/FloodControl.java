
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("flood_control")
public class FloodControl {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("item_type")
    private String itemType;

    private String name;

    private String content;

    private String location;

    private String images;

    @TableField("create_time")
    private LocalDateTime createTime;
}
