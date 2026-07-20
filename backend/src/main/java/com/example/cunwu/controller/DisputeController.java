
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Dispute;
import com.example.cunwu.mapper.DisputeMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispute")
public class DisputeController {

    private final DisputeMapper disputeMapper;

    public DisputeController(DisputeMapper disputeMapper) {
        this.disputeMapper = disputeMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Dispute>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Dispute> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Dispute::getDisputeType, type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Dispute::getStatus, status);
        }
        wrapper.orderByDesc(Dispute::getCreateTime);
        return Result.success(disputeMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Dispute> getById(@PathVariable Long id) {
        return Result.success(disputeMapper.selectById(id));
    }

    @PostMapping("/register")
    public Result<Dispute> register(@RequestBody Dispute dispute) {
        dispute.setStatus("待调解");
        dispute.setCreateTime(LocalDateTime.now());
        disputeMapper.insert(dispute);
        return Result.success("登记成功", dispute);
    }

    @PutMapping("/assign/{id}")
    public Result<Dispute> assign(@PathVariable Long id, @RequestParam String handler) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }
        dispute.setHandler(handler);
        dispute.setStatus("调解中");
        disputeMapper.updateById(dispute);
        return Result.success("分配成功", dispute);
    }

    @PutMapping("/mediate/{id}")
    public Result<Dispute> mediate(@PathVariable Long id, @RequestBody Dispute update) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }
        dispute.setMediationRecord(update.getMediationRecord());
        dispute.setImages(update.getImages());
        disputeMapper.updateById(dispute);
        return Result.success("调解记录已保存", dispute);
    }

    @PutMapping("/resolve/{id}")
    public Result<Dispute> resolve(@PathVariable Long id, @RequestParam String result) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }
        dispute.setResult(result);
        dispute.setStatus("已调解");
        disputeMapper.updateById(dispute);
        return Result.success("已成功调解", dispute);
    }

    @PutMapping("/ongoing/{id}")
    public Result<Dispute> ongoing(@PathVariable Long id) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }
        dispute.setStatus("未化解");
        disputeMapper.updateById(dispute);
        return Result.success("已标记为持续跟踪，将推送乡镇督办", dispute);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        disputeMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/tracking")
    public Result<Map<String, Object>> getTrackingList() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        
        Map<String, Object> item = new HashMap<>();
        item.put("id", 1);
        item.put("title", "张三与李四土地纠纷");
        item.put("createTime", "2026-07-01");
        item.put("duration", 30);
        item.put("status", "跟踪中");
        list.add(item);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", 5);
        stats.put("over30", 2);
        stats.put("over60", 1);
        
        data.put("list", list);
        data.put("stats", stats);
        return Result.success(data);
    }
}
