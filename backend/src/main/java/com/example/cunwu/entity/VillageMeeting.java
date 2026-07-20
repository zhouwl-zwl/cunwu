
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("village_meeting")
public class VillageMeeting {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("meeting_type")
    private String meetingType;

    private String content;

    private String participants;

    private String images;

    @TableField("meeting_date")
    private LocalDateTime meetingDate;

    @TableField("create_time")
    private LocalDateTime createTime;
}
