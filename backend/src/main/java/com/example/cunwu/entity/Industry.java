
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("industry")
public class Industry {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;

    private String content;

    private String images;

    private Double income;

    private String responsiblePerson;

    @TableField("create_time")
    private LocalDateTime createTime;
}
