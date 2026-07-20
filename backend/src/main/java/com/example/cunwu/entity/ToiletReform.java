
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("toilet_reform")
public class ToiletReform {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("household_name")
    private String householdName;

    private String address;

    @TableField("reform_type")
    private String reformType;

    @TableField("before_images")
    private String beforeImages;

    @TableField("mid_images")
    private String midImages;

    @TableField("after_images")
    private String afterImages;

    @TableField("verify_status")
    private String verifyStatus;

    @TableField("subsidy_amount")
    private Double subsidyAmount;

    @TableField("create_time")
    private LocalDateTime createTime;

    private String status;

    @TableField("village_verify_opinion")
    private String villageVerifyOpinion;

    @TableField("village_verifier")
    private String villageVerifier;

    @TableField("village_verify_time")
    private LocalDateTime villageVerifyTime;

    @TableField("town_verify_opinion")
    private String townVerifyOpinion;

    @TableField("town_verifier")
    private String townVerifier;

    @TableField("town_verify_time")
    private LocalDateTime townVerifyTime;

    @TableField("subsidy_status")
    private String subsidyStatus;

    @TableField("subsidy_voucher")
    private String subsidyVoucher;

    @TableField("subsidy_time")
    private LocalDateTime subsidyTime;
}
