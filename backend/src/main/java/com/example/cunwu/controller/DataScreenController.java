package com.example.cunwu.controller;

import com.example.cunwu.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data-screen")
public class DataScreenController {

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalPopulation", 2580);
        data.put("householdCount", 680);
        data.put("partyMemberCount", 120);
        data.put("assetTotal", 2850000);
        data.put("demandCount", 2);
        data.put("appointmentCount", 1);
        data.put("disputeCount", 0);
        data.put("subsidyCount", 150);
        return Result.success(data);
    }
}
