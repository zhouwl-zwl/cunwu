
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task_supervision")
public class TaskSupervision {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String source;

    private String content;

    private LocalDateTime deadline;

    private String status;

    private String assignee;

    private String result;

    @TableField("create_time")
    private LocalDateTime createTime;
}
