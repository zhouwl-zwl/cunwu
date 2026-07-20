
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.*;
import com.example.cunwu.mapper.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class HomeController {

    private final VillageInfoMapper villageInfoMapper;
    private final OrgMemberMapper orgMemberMapper;
    private final PublicNoticeMapper publicNoticeMapper;
    private final NotificationMapper notificationMapper;
    private final AssetMapper assetMapper;
    private final IndustryMapper industryMapper;

    public HomeController(VillageInfoMapper villageInfoMapper, OrgMemberMapper orgMemberMapper,
                         PublicNoticeMapper publicNoticeMapper, NotificationMapper notificationMapper,
                         AssetMapper assetMapper, IndustryMapper industryMapper) {
        this.villageInfoMapper = villageInfoMapper;
        this.orgMemberMapper = orgMemberMapper;
        this.publicNoticeMapper = publicNoticeMapper;
        this.notificationMapper = notificationMapper;
        this.assetMapper = assetMapper;
        this.industryMapper = industryMapper;
    }

    @GetMapping("/village-info")
    public Result<VillageInfo> getVillageInfo() {
        List<VillageInfo> list = villageInfoMapper.selectList(null);
        VillageInfo info = list.isEmpty() ? null : list.get(0);
        return Result.success(info);
    }

    @GetMapping("/org-members")
    public Result<List<OrgMember>> getOrgMembers(@RequestParam(required = false) String orgType) {
        LambdaQueryWrapper<OrgMember> wrapper = new LambdaQueryWrapper<>();
        if (orgType != null && !orgType.isEmpty()) {
            wrapper.eq(OrgMember::getOrgType, orgType);
        }
        return Result.success(orgMemberMapper.selectList(wrapper));
    }

    @GetMapping("/public-notices")
    public Result<IPage<PublicNotice>> getPublicNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String noticeType) {
        
        LambdaQueryWrapper<PublicNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PublicNotice::getStatus, 1);
        if (noticeType != null && !noticeType.isEmpty()) {
            wrapper.eq(PublicNotice::getNoticeType, noticeType);
        }
        wrapper.orderByDesc(PublicNotice::getCreateTime);
        
        return Result.success(publicNoticeMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/notifications")
    public Result<IPage<Notification>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getStatus, 1);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Notification::getType, type);
        }
        wrapper.orderByDesc(Notification::getIsTop).orderByDesc(Notification::getCreateTime);
        
        return Result.success(notificationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/home-data")
    public Result<Map<String, Object>> getHomeData() {
        Map<String, Object> data = new HashMap<>();
        
        List<VillageInfo> villageList = villageInfoMapper.selectList(null);
        data.put("villageInfo", villageList.isEmpty() ? null : villageList.get(0));
        
        LambdaQueryWrapper<OrgMember> orgWrapper = new LambdaQueryWrapper<>();
        orgWrapper.eq(OrgMember::getOrgType, "支委");
        data.put("orgMembers", orgMemberMapper.selectList(orgWrapper));
        
        LambdaQueryWrapper<PublicNotice> noticeWrapper = new LambdaQueryWrapper<>();
        noticeWrapper.eq(PublicNotice::getStatus, 1);
        noticeWrapper.last("LIMIT 6");
        data.put("publicNotices", publicNoticeMapper.selectList(noticeWrapper));
        
        LambdaQueryWrapper<Notification> notifyWrapper = new LambdaQueryWrapper<>();
        notifyWrapper.eq(Notification::getStatus, 1);
        notifyWrapper.orderByDesc(Notification::getIsTop).orderByDesc(Notification::getCreateTime);
        notifyWrapper.last("LIMIT 5");
        data.put("notifications", notificationMapper.selectList(notifyWrapper));
        
        data.put("totalAssets", assetMapper.selectCount(null));
        data.put("industries", industryMapper.selectList(null));
        
        return Result.success(data);
    }

    @GetMapping("/policy-list")
    public Result<IPage<PublicNotice>> getPolicyList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        
        LambdaQueryWrapper<PublicNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PublicNotice::getStatus, 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(PublicNotice::getTitle, keyword)
                    .or().like(PublicNotice::getContent, keyword));
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(PublicNotice::getNoticeType, category);
        }
        wrapper.orderByDesc(PublicNotice::getCreateTime);
        
        return Result.success(publicNoticeMapper.selectPage(new Page<>(page, size), wrapper));
    }
}
