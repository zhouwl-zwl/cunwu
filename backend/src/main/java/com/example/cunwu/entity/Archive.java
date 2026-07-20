
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("archive")
public class Archive {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String category;

    private String source;

    private String content;

    private String keywords;

    private String attachments;

    @TableField("upload_date")
    private LocalDateTime uploadDate;

    @TableField("create_time")
    private LocalDateTime createTime;
}
