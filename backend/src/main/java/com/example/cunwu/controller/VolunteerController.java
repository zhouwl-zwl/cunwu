package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteer")
public class VolunteerController {

    @GetMapping("/my-stats")
    public Result<Map<String, Object>> getMyStats(@RequestParam(required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("hours", 12);
        data.put("points", 120);
        return Result.success(data);
    }

    @GetMapping("/members")
    public Result<Map<String, Object>> getMembers(@RequestParam(required = false) String keyword) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new java.util.ArrayList<>());
        return Result.success(data);
    }

    @GetMapping("/activities")
    public Result<Map<String, Object>> getActivities() {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new java.util.ArrayList<>());
        return Result.success(data);
    }

    @GetMapping("/points")
    public Result<Map<String, Object>> getPoints() {
        Map<String, Object> data = new HashMap<>();
        data.put("goods", new java.util.ArrayList<>());
        data.put("records", new java.util.ArrayList<>());
        return Result.success(data);
    }
}
