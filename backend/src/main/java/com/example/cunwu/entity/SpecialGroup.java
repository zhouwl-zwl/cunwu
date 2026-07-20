
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("special_group")
public class SpecialGroup {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("group_type")
    private String groupType;

    private String phone;

    private String address;

    @TableField("visit_record")
    private String visitRecord;

    @TableField("last_visit_date")
    private LocalDateTime lastVisitDate;

    @TableField("create_time")
    private LocalDateTime createTime;
}
