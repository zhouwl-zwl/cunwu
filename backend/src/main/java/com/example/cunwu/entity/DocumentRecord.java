
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("document_record")
public class DocumentRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("doc_type")
    private String docType;

    @TableField("doc_no")
    private String docNo;

    private String source;

    private String content;

    private String files;

    private LocalDateTime deadline;

    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
