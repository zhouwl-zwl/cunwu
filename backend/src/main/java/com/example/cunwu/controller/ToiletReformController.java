
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.ToiletReform;
import com.example.cunwu.mapper.ToiletReformMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/toilet-reform")
public class ToiletReformController {

    private final ToiletReformMapper toiletReformMapper;

    public ToiletReformController(ToiletReformMapper toiletReformMapper) {
        this.toiletReformMapper = toiletReformMapper;
    }

    @GetMapping("/list")
    public Result<IPage<ToiletReform>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<ToiletReform> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ToiletReform::getReformType, type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ToiletReform::getStatus, status);
        }
        wrapper.orderByDesc(ToiletReform::getCreateTime);
        return Result.success(toiletReformMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<ToiletReform> getById(@PathVariable Long id) {
        return Result.success(toiletReformMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<ToiletReform> add(@RequestBody ToiletReform toiletReform) {
        toiletReform.setStatus("待改造");
        toiletReform.setCreateTime(LocalDateTime.now());
        toiletReformMapper.insert(toiletReform);
        return Result.success("新增改造户成功", toiletReform);
    }

    @PutMapping("/upload-images/{id}")
    public Result<ToiletReform> uploadImages(@PathVariable Long id, @RequestBody Map<String, String> images) {
        ToiletReform reform = toiletReformMapper.selectById(id);
        if (reform == null) {
            return Result.error("记录不存在");
        }
        if (images.containsKey("beforeImages")) {
            reform.setBeforeImages(images.get("beforeImages"));
        }
        if (images.containsKey("midImages")) {
            reform.setMidImages(images.get("midImages"));
            reform.setStatus("改造中");
        }
        if (images.containsKey("afterImages")) {
            reform.setAfterImages(images.get("afterImages"));
            reform.setStatus("待验收");
        }
        toiletReformMapper.updateById(reform);
        return Result.success("图片上传成功", reform);
    }

    @PutMapping("/village-verify/{id}")
    public Result<ToiletReform> villageVerify(@PathVariable Long id, @RequestParam String verifier, 
                                               @RequestParam String opinion, @RequestParam boolean approved) {
        ToiletReform reform = toiletReformMapper.selectById(id);
        if (reform == null) {
            return Result.error("记录不存在");
        }
        reform.setVillageVerifier(verifier);
        reform.setVillageVerifyOpinion(opinion);
        reform.setVillageVerifyTime(LocalDateTime.now());
        if (approved) {
            reform.setStatus("待乡镇验收");
        } else {
            reform.setStatus("整改中");
        }
        toiletReformMapper.updateById(reform);
        return Result.success(approved ? "村验收通过，等待乡镇验收" : "需整改，请重新上传改造后照片", reform);
    }

    @PutMapping("/town-verify/{id}")
    public Result<ToiletReform> townVerify(@PathVariable Long id, @RequestParam String verifier, 
                                            @RequestParam String opinion, @RequestParam boolean approved,
                                            @RequestParam(required = false) Double subsidyAmount) {
        ToiletReform reform = toiletReformMapper.selectById(id);
        if (reform == null) {
            return Result.error("记录不存在");
        }
        reform.setTownVerifier(verifier);
        reform.setTownVerifyOpinion(opinion);
        reform.setTownVerifyTime(LocalDateTime.now());
        if (approved) {
            reform.setStatus("已验收");
            reform.setVerifyStatus("已验收");
            if (subsidyAmount != null) {
                reform.setSubsidyAmount(subsidyAmount);
            }
            reform.setSubsidyStatus("待发放");
        } else {
            reform.setStatus("整改中");
        }
        toiletReformMapper.updateById(reform);
        return Result.success(approved ? "乡镇验收通过，等待发放补助" : "需整改，请重新提交", reform);
    }

    @PutMapping("/subsidy/{id}")
    public Result<ToiletReform> subsidy(@PathVariable Long id, @RequestParam String voucher) {
        ToiletReform reform = toiletReformMapper.selectById(id);
        if (reform == null) {
            return Result.error("记录不存在");
        }
        reform.setSubsidyVoucher(voucher);
        reform.setSubsidyStatus("已发放");
        reform.setSubsidyTime(LocalDateTime.now());
        reform.setStatus("已完成");
        toiletReformMapper.updateById(reform);
        return Result.success("补助已发放，流程完成", reform);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        long total = toiletReformMapper.selectCount(null);
        
        LambdaQueryWrapper<ToiletReform> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(ToiletReform::getStatus, "已完成");
        long completed = toiletReformMapper.selectCount(completedWrapper);
        
        LambdaQueryWrapper<ToiletReform> verifyingWrapper = new LambdaQueryWrapper<>();
        verifyingWrapper.eq(ToiletReform::getStatus, "待验收").or().eq(ToiletReform::getStatus, "待乡镇验收");
        long verifying = toiletReformMapper.selectCount(verifyingWrapper);
        
        Double totalSubsidy = toiletReformMapper.selectList(null)
                .stream().mapToDouble(t -> t.getSubsidyAmount() != null ? t.getSubsidyAmount() : 0).sum();
        
        result.put("total", total);
        result.put("completed", completed);
        result.put("verifying", verifying);
        result.put("totalSubsidy", totalSubsidy);
        result.put("completionRate", total > 0 ? (completed * 100.0 / total) : 0);
        
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        toiletReformMapper.deleteById(id);
        return Result.success();
    }
}

