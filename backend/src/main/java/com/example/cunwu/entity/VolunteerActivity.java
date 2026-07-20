
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("volunteer_activity")
public class VolunteerActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("activity_type")
    private String activityType;

    private String content;

    private String location;

    @TableField("activity_date")
    private LocalDateTime activityDate;

    private Integer participants;

    @TableField("total_hours")
    private Integer totalHours;

    private String images;

    @TableField("create_time")
    private LocalDateTime createTime;
}
