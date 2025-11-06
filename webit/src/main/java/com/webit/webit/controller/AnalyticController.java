package com.webit.webit.controller;


import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.analytic.EmployerAnalytics;
import com.webit.webit.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ApiResponse<EmployerAnalytics> analytics() {
        return ApiResponse.<EmployerAnalytics>builder()
                .result(analyticsService.getAnalytics())
                .build();
    }
}
