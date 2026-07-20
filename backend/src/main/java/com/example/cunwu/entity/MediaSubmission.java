
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("media_submission")
public class MediaSubmission {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("target_media")
    private String targetMedia;

    private String content;

    @TableField("submit_date")
    private LocalDateTime submitDate;

    @TableField("publish_status")
    private String publishStatus;

    @TableField("publish_date")
    private LocalDateTime publishDate;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
