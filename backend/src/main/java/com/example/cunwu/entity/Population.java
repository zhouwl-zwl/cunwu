
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("population")
public class Population {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("household_name")
    private String householdName;

    @TableField("id_card")
    private String idCard;

    private String gender;

    private Integer age;

    private String phone;

    private String address;

    @TableField("family_members")
    private Integer familyMembers;

    @TableField("is_permanent")
    private String isPermanent;

    @TableField("work_location")
    private String workLocation;

    @TableField("group_name")
    private String groupName;

    @TableField("create_time")
    private LocalDateTime createTime;
}
