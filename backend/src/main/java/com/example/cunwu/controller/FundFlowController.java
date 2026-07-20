package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.FundFlow;
import com.example.cunwu.mapper.FundFlowMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fund")
public class FundFlowController {

    private final FundFlowMapper fundFlowMapper;

    public FundFlowController(FundFlowMapper fundFlowMapper) {
        this.fundFlowMapper = fundFlowMapper;
    }

    @GetMapping("/list")
    public Result<IPage<FundFlow>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<FundFlow> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(FundFlow::getType, type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(FundFlow::getStatus, status);
        }
        wrapper.orderByDesc(FundFlow::getCreateTime);
        return Result.success(fundFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<FundFlow> getById(@PathVariable Long id) {
        return Result.success(fundFlowMapper.selectById(id));
    }

    @PostMapping("/submit")
    public Result<FundFlow> submit(@RequestBody FundFlow fundFlow) {
        fundFlow.setStatus("待审核");
        fundFlow.setType("EXPENSE");
        fundFlow.setCreateTime(LocalDateTime.now());
        fundFlow.setFlowDate(LocalDateTime.now());
        fundFlowMapper.insert(fundFlow);
        return Result.success("提交成功，等待村主任审核", fundFlow);
    }

    @PutMapping("/audit/{id}")
    public Result<FundFlow> audit(@PathVariable Long id, @RequestParam String auditor, 
                                   @RequestParam String opinion, @RequestParam boolean approved) {
        FundFlow fundFlow = fundFlowMapper.selectById(id);
        if (fundFlow == null) {
            return Result.error("记录不存在");
        }
        fundFlow.setAuditor(auditor);
        fundFlow.setAuditOpinion(opinion);
        fundFlow.setAuditTime(LocalDateTime.now());
        if (approved) {
            fundFlow.setStatus("待复核");
        } else {
            fundFlow.setStatus("已驳回");
        }
        fundFlowMapper.updateById(fundFlow);
        return Result.success(approved ? "审核通过，等待监委复核" : "已驳回，请修改后重新提交", fundFlow);
    }

    @PutMapping("/review/{id}")
    public Result<FundFlow> review(@PathVariable Long id, @RequestParam String reviewer, 
                                    @RequestParam String opinion, @RequestParam boolean approved) {
        FundFlow fundFlow = fundFlowMapper.selectById(id);
        if (fundFlow == null) {
            return Result.error("记录不存在");
        }
        fundFlow.setReviewer(reviewer);
        fundFlow.setReviewOpinion(opinion);
        fundFlow.setReviewTime(LocalDateTime.now());
        if (approved) {
            fundFlow.setStatus("已入账");
        } else {
            fundFlow.setStatus("已驳回");
        }
        fundFlowMapper.updateById(fundFlow);
        return Result.success(approved ? "复核通过，已入账" : "已驳回，请修改后重新提交", fundFlow);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam(required = false) String month) {
        Map<String, Object> result = new HashMap<>();
        
        LambdaQueryWrapper<FundFlow> incomeWrapper = new LambdaQueryWrapper<>();
        incomeWrapper.eq(FundFlow::getType, "INCOME");
        Double totalIncome = fundFlowMapper.selectList(incomeWrapper)
                .stream().mapToDouble(FundFlow::getAmount).sum();
        
        LambdaQueryWrapper<FundFlow> expenseWrapper = new LambdaQueryWrapper<>();
        expenseWrapper.eq(FundFlow::getType, "EXPENSE");
        Double totalExpense = fundFlowMapper.selectList(expenseWrapper)
                .stream().mapToDouble(FundFlow::getAmount).sum();
        
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome - totalExpense);
        
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fundFlowMapper.deleteById(id);
        return Result.success();
    }
}
