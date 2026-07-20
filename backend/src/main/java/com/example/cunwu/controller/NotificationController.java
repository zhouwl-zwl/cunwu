
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Notification;
import com.example.cunwu.mapper.NotificationMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Notification>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getStatus, 1);
        wrapper.and(w -> w.isNull(Notification::getUserId));
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Notification::getType, type);
        }
        wrapper.orderByDesc(Notification::getIsTop).orderByDesc(Notification::getCreateTime);
        
        return Result.success(notificationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/user-list")
    public Result<IPage<Notification>> getUserNotifications(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getStatus, 1);
        wrapper.and(w -> w.eq(Notification::getUserId, userId).or().isNull(Notification::getUserId));
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Notification::getType, type);
        }
        wrapper.orderByDesc(Notification::getIsTop).orderByDesc(Notification::getCreateTime);
        
        return Result.success(notificationMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Notification> getNotificationById(@PathVariable Long id) {
        return Result.success(notificationMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<Notification> addNotification(@RequestBody Notification notification) {
        notification.setStatus(1);
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);
        return Result.success("添加成功", notification);
    }

    @PutMapping("/update")
    public Result<Notification> updateNotification(@RequestBody Notification notification) {
        notificationMapper.updateById(notification);
        return Result.success("更新成功", notification);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNotification(@PathVariable Long id) {
        notificationMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/top/{id}")
    public Result<Void> toggleTop(@PathVariable Long id, @RequestParam Integer isTop) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null) {
            notification.setIsTop(isTop);
            notificationMapper.updateById(notification);
        }
        return Result.success();
    }
}
