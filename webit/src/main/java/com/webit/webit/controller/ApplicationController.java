package com.webit.webit.controller;


import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.application.ApplicationResponse;
import com.webit.webit.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/{jobId}")
    public ApiResponse<ApplicationResponse> applyJob(@PathVariable String jobId) {


        return ApiResponse.<ApplicationResponse>builder()
                .result(applicationService.createApplication(jobId))
                .build();
    }
}
