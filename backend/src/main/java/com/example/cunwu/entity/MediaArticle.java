
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("media_article")
public class MediaArticle {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("article_type")
    private String articleType;

    private String content;

    private String images;

    private String author;

    @TableField("publish_date")
    private LocalDateTime publishDate;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
