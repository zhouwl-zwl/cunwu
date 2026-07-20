
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.WarehouseFlow;
import com.example.cunwu.mapper.WarehouseFlowMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/warehouse-flow")
public class WarehouseFlowController {

    private final WarehouseFlowMapper warehouseFlowMapper;

    public WarehouseFlowController(WarehouseFlowMapper warehouseFlowMapper) {
        this.warehouseFlowMapper = warehouseFlowMapper;
    }

    @GetMapping("/list")
    public Result<IPage<WarehouseFlow>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<WarehouseFlow> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(WarehouseFlow::getItemName, keyword)
                    .or().like(WarehouseFlow::getFlowType, keyword)
                    .or().like(WarehouseFlow::getApplicant, keyword)
                    .or().like(WarehouseFlow::getPurpose, keyword));
        }
        wrapper.orderByDesc(WarehouseFlow::getCreateTime);
        return Result.success(warehouseFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<WarehouseFlow> getById(@PathVariable Long id) {
        return Result.success(warehouseFlowMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<WarehouseFlow> add(@RequestBody WarehouseFlow warehouseFlow) {
        warehouseFlow.setCreateTime(LocalDateTime.now());
        warehouseFlowMapper.insert(warehouseFlow);
        return Result.success("新增成功", warehouseFlow);
    }

    @PutMapping("/update")
    public Result<WarehouseFlow> update(@RequestBody WarehouseFlow warehouseFlow) {
        warehouseFlowMapper.updateById(warehouseFlow);
        return Result.success("更新成功", warehouseFlow);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseFlowMapper.deleteById(id);
        return Result.success();
    }
}
