
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("forest_fire")
public class ForestFire {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("patrol_person")
    private String patrolPerson;

    @TableField("patrol_date")
    private LocalDateTime patrolDate;

    private String route;

    private String content;

    private String images;

    private String issues;

    @TableField("create_time")
    private LocalDateTime createTime;
}
