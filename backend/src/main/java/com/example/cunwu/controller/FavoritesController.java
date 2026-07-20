package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    @GetMapping("/list")
    public Result<Map<String, Object>> getList(@RequestParam(required = false) Long userId) {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", records.size());
        return Result.success(data);
    }
}
