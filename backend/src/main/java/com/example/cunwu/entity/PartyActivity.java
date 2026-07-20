
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("party_activity")
public class PartyActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("activity_type")
    private String activityType;

    private String content;

    private String participants;

    private String images;

    @TableField("activity_date")
    private LocalDateTime activityDate;

    private String recorder;

    @TableField("create_time")
    private LocalDateTime createTime;
}
