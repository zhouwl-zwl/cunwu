package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.WarehouseItem;
import com.example.cunwu.entity.WarehouseFlow;
import com.example.cunwu.mapper.WarehouseItemMapper;
import com.example.cunwu.mapper.WarehouseFlowMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final WarehouseItemMapper warehouseItemMapper;
    private final WarehouseFlowMapper warehouseFlowMapper;

    public WarehouseController(WarehouseItemMapper warehouseItemMapper, WarehouseFlowMapper warehouseFlowMapper) {
        this.warehouseItemMapper = warehouseItemMapper;
        this.warehouseFlowMapper = warehouseFlowMapper;
    }

    @GetMapping("/stock")
    public Result<IPage<WarehouseItem>> getStock(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<WarehouseItem> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(WarehouseItem::getName, keyword)
                    .or().like(WarehouseItem::getCategory, keyword));
        }
        wrapper.orderByDesc(WarehouseItem::getCreateTime);
        return Result.success(warehouseItemMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/inbound")
    public Result<IPage<WarehouseFlow>> getInbound(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<WarehouseFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseFlow::getFlowType, "INBOUND");
        wrapper.orderByDesc(WarehouseFlow::getCreateTime);
        return Result.success(warehouseFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/borrow")
    public Result<Map<String, Object>> getBorrow() {
        Map<String, Object> data = new HashMap<>();
        
        LambdaQueryWrapper<WarehouseFlow> borrowWrapper = new LambdaQueryWrapper<>();
        borrowWrapper.eq(WarehouseFlow::getFlowType, "BORROW");
        data.put("borrowRecords", warehouseFlowMapper.selectList(borrowWrapper));
        
        LambdaQueryWrapper<WarehouseFlow> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(WarehouseFlow::getFlowType, "BORROW");
        pendingWrapper.isNull(WarehouseFlow::getReturnDate);
        data.put("pendingReturn", pendingWrapper.getEntityClass());
        
        return Result.success(data);
    }

    @GetMapping("/inventory")
    public Result<IPage<WarehouseFlow>> getInventory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<WarehouseFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseFlow::getFlowType, "INVENTORY");
        wrapper.orderByDesc(WarehouseFlow::getCreateTime);
        return Result.success(warehouseFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/scrap")
    public Result<IPage<WarehouseFlow>> getScrap(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LambdaQueryWrapper<WarehouseFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseFlow::getFlowType, "SCRAP");
        wrapper.orderByDesc(WarehouseFlow::getCreateTime);
        return Result.success(warehouseFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }
}