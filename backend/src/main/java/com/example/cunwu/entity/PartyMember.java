
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("party_member")
public class PartyMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String gender;

    @TableField("birth_date")
    private LocalDateTime birthDate;

    @TableField("join_date")
    private LocalDateTime joinDate;

    private String phone;

    private String address;

    @TableField("party_type")
    private String partyType;

    @TableField("org_relation_status")
    private String orgRelationStatus;

    private Integer score;

    @TableField("create_time")
    private LocalDateTime createTime;
}
