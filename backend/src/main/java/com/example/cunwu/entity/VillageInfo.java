
package com.example.cunwu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("village_info")
public class VillageInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String villageName;

    private Integer population;

    private Double area;

    private Integer householdCount;

    private String groupCount;

    private String location;

    private String history;

    private String honors;

    private String products;

    private Double totalAssets;

    @TableField("carousel_images")
    private String carouselImages;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
