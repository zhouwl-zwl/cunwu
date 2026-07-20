
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.Asset;
import com.example.cunwu.entity.FundFlow;
import com.example.cunwu.entity.Resource;
import com.example.cunwu.mapper.AssetMapper;
import com.example.cunwu.mapper.FundFlowMapper;
import com.example.cunwu.mapper.ResourceMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AssetMapper assetMapper;
    private final FundFlowMapper fundFlowMapper;
    private final ResourceMapper resourceMapper;

    public AssetController(AssetMapper assetMapper, FundFlowMapper fundFlowMapper, ResourceMapper resourceMapper) {
        this.assetMapper = assetMapper;
        this.fundFlowMapper = fundFlowMapper;
        this.resourceMapper = resourceMapper;
    }

    @GetMapping("/list")
    public Result<IPage<Asset>> getAssetList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Asset::getType, type);
        }
        wrapper.orderByDesc(Asset::getCreateTime);
        
        return Result.success(assetMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PostMapping("/add")
    public Result<Asset> addAsset(@RequestBody Asset asset) {
        asset.setCreateTime(LocalDateTime.now());
        assetMapper.insert(asset);
        return Result.success("添加成功", asset);
    }

    @PutMapping("/update")
    public Result<Asset> updateAsset(@RequestBody Asset asset) {
        assetMapper.updateById(asset);
        return Result.success("更新成功", asset);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAsset(@PathVariable Long id) {
        assetMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/fund-flow")
    public Result<IPage<FundFlow>> getFundFlow(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<FundFlow> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(FundFlow::getType, type);
        }
        wrapper.orderByDesc(FundFlow::getFlowDate);
        
        return Result.success(fundFlowMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/resource")
    public Result<IPage<Resource>> getResource(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Resource::getType, type);
        }
        wrapper.orderByDesc(Resource::getCreateTime);
        
        return Result.success(resourceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        Double assetTotal = assetMapper.selectList(null).stream()
                .mapToDouble(Asset::getValue).sum();
        data.put("assetTotal", assetTotal);
        
        Double incomeTotal = fundFlowMapper.selectList(new LambdaQueryWrapper<FundFlow>()
                .eq(FundFlow::getType, "INCOME")).stream()
                .mapToDouble(FundFlow::getAmount).sum();
        data.put("incomeTotal", incomeTotal);
        
        Double expenseTotal = fundFlowMapper.selectList(new LambdaQueryWrapper<FundFlow>()
                .eq(FundFlow::getType, "EXPENSE")).stream()
                .mapToDouble(FundFlow::getAmount).sum();
        data.put("expenseTotal", expenseTotal);
        
        data.put("assetCount", assetMapper.selectCount(null));
        data.put("resourceCount", resourceMapper.selectCount(null));
        
        return Result.success(data);
    }

    @GetMapping("/detail/{id}")
    public Result<Object> getDetail(@PathVariable Long id, @RequestParam(required = false) String type) {
        if ("resource".equals(type)) {
            return Result.success(resourceMapper.selectById(id));
        }
        return Result.success(assetMapper.selectById(id));
    }
}
