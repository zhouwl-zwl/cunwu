
package com.example.cunwu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cunwu.common.Result;
import com.example.cunwu.entity.PensionInsurance;
import com.example.cunwu.mapper.PensionInsuranceMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pension-insurance")
public class PensionInsuranceController {

    private final PensionInsuranceMapper pensionInsuranceMapper;

    public PensionInsuranceController(PensionInsuranceMapper pensionInsuranceMapper) {
        this.pensionInsuranceMapper = pensionInsuranceMapper;
    }

    @GetMapping("/list")
    public Result<IPage<PensionInsurance>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<PensionInsurance> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(PensionInsurance::getPersonName, keyword)
                    .or().like(PensionInsurance::getIdCard, keyword)
                    .or().like(PensionInsurance::getPhone, keyword)
                    .or().like(PensionInsurance::getInsuranceType, keyword));
        }
        wrapper.orderByDesc(PensionInsurance::getCreateTime);
        return Result.success(pensionInsuranceMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<PensionInsurance> getById(@PathVariable Long id) {
        return Result.success(pensionInsuranceMapper.selectById(id));
    }

    @PostMapping("/add")
    public Result<PensionInsurance> add(@RequestBody PensionInsurance pensionInsurance) {
        pensionInsurance.setCreateTime(LocalDateTime.now());
        pensionInsuranceMapper.insert(pensionInsurance);
        return Result.success("新增成功", pensionInsurance);
    }

    @PutMapping("/update")
    public Result<PensionInsurance> update(@RequestBody PensionInsurance pensionInsurance) {
        pensionInsuranceMapper.updateById(pensionInsurance);
        return Result.success("更新成功", pensionInsurance);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        pensionInsuranceMapper.deleteById(id);
        return Result.success();
    }
}
