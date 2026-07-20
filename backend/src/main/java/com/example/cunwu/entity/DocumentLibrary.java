
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("document_library")
public class DocumentLibrary {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String category;

    @TableField("file_path")
    private String filePath;

    @TableField("file_type")
    private String fileType;

    private String description;

    @TableField("create_time")
    private LocalDateTime createTime;
}
