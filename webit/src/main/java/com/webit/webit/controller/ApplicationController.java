package com.webit.webit.controller;


import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.application.ApplicationResponse;
import com.webit.webit.dto.response.application.ApplicationResponseById;
import com.webit.webit.dto.response.application.MyApplicationResponse;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/my")
    public ApiResponse<List<MyApplicationResponse>> myApplication() {
        return ApiResponse.<List<MyApplicationResponse>>builder()
                .result(applicationService.getMyApplication())
                .build();
    }


    @GetMapping("/{applicationId}")
    public ApiResponse<ApplicationResponseById> getApplicationById(@PathVariable String applicationId) {
        return ApiResponse.<ApplicationResponseById>builder()
                .result(applicationService.getApplication(applicationId))
                .build();
    }

    @GetMapping("/job/{jobId}")
    public ApiResponse<PageResponse<?>> getApplicants(@RequestParam int pageNo, @RequestParam int pageSize, @PathVariable String jobId) {
        return ApiResponse.<PageResponse<?>>builder()
                .result(applicationService.getApplicants(pageNo, pageSize, jobId))
                .build();
    }

    @PutMapping("/{applicationId}/status")
    public ApiResponse<String> updateStatus(@RequestParam Status status, @PathVariable String applicationId) {
        return ApiResponse.<String>builder()
                .message("Application status updated")
                .result(applicationService.updateStatus(applicationId, status))
                .build();
    }

}
