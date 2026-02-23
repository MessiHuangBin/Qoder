package com.example.springbootmybatis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "测试成功");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/time/next-year")
    public Map<String, Object> getNextYearTime() {
        Map<String, Object> response = new HashMap<>();
        
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 计算一年后的时间
        LocalDateTime nextYear = now.plusYears(1);
        
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = nextYear.format(formatter);
        
        response.put("currentTime", now.format(formatter));
        response.put("nextYearTime", formattedTime);
        response.put("nextYearTimestamp", nextYear.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        response.put("message", "当前时间一年后的时间");
        response.put("status", "success");
        
        return response;
    }

    @GetMapping("/time/current-seconds")
    public Map<String, Object> getCurrentSeconds() {
        Map<String, Object> response = new HashMap<>();
        
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 提取秒数(0-59)
        int currentSeconds = now.getSecond();
        
        // 格式化完整时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
        
        response.put("currentSeconds", currentSeconds);
        response.put("currentTime", formattedTime);
        response.put("timestamp", now.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        response.put("message", "当前时间的秒数");
        response.put("status", "success");
        
        return response;
    }
}