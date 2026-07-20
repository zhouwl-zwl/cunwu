
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.WarehouseItem;
import com.example.cunwu.mapper.WarehouseItemMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/warehouse-item")
public class WarehouseItemController {

    private final WarehouseItemMapper warehouseItemMapper;

    public WarehouseItemController(WarehouseItemMapper warehouseItemMapper) {
        this.warehouseItemMapper = warehouseItemMapper;
    }

    @GetMapping("/list")
    public Result<IPage<WarehouseItem>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<WarehouseItem> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(WarehouseItem::getName, keyword)
                    .or().like(WarehouseItem::getCategory, keyword)
                    .or().like(WarehouseItem::getStorageLocation, keyword));
        }
        wrapper.orderByDesc(WarehouseItem::getCreateTime);
        return Result.success(warehouseItemMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<WarehouseItem> getById(@PathVariable Long id) {
        return Result.success(warehouseItemMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<WarehouseItem> add(@RequestBody WarehouseItem warehouseItem) {
        warehouseItem.setCreateTime(LocalDateTime.now());
        warehouseItemMapper.insert(warehouseItem);
        return Result.success("新增成功", warehouseItem);
    }

    @PutMapping("/update")
    public Result<WarehouseItem> update(@RequestBody WarehouseItem warehouseItem) {
        warehouseItemMapper.updateById(warehouseItem);
        return Result.success("更新成功", warehouseItem);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseItemMapper.deleteById(id);
        return Result.success();
    }
}
