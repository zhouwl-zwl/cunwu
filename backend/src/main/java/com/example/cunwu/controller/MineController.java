package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mine")
public class MineController {

    @GetMapping("/counts")
    public Result<Map<String, Object>> getCounts(@RequestParam(required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("demandCount", 2);
        data.put("appointmentCount", 1);
        data.put("unreadCount", 2);
        return Result.success(data);
    }
}
