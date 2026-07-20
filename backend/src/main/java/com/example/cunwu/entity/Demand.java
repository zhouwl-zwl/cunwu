
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("demand")
public class Demand {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String phone;

    private String demandType;

    private String content;

    private String images;

    private String status;

    @TableField("handler_id")
    private Long handlerId;

    private String handlerName;

    private String solution;

    private String resultImages;

    @TableField("user_id")
    private Long userId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
