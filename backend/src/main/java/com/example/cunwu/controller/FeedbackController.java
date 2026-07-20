package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @PostMapping("/submit")
    public Result<Map<String, Object>> submit(@RequestBody Map<String, Object> body) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        return Result.success("反馈提交成功", data);
    }
}
