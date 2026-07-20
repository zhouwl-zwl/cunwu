
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("org_member")
public class OrgMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String avatar;

    private String position;

    private String division;

    private String phone;

    private String orgType;

    @TableField("create_time")
    private LocalDateTime createTime;
}
