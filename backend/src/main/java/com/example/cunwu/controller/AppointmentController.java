package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @PostMapping("/submit")
    public Result<Map<String, Object>> submit(@RequestBody Map<String, Object> body) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("status", "待审核");
        return Result.success("预约提交成功", data);
    }

    @GetMapping("/my-list")
    public Result<Map<String, Object>> getMyList(@RequestParam(required = false) Long userId) {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", 1);
        item.put("name", "预约办事");
        item.put("date", "2026-07-20");
        item.put("time", "下午2:00");
        item.put("status", "待审核");
        records.add(item);

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", records.size());
        return Result.success(data);
    }

    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id) {
        return Result.success("预约已取消");
    }
}
