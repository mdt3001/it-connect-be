package com.webit.webit.controller;


import com.webit.webit.dto.request.job.JobCreateRequest;
import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobCreateResponse;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.service.JobService;
import com.webit.webit.service.SavedJobService;
import com.webit.webit.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private final SavedJobService savedJobService;

    private final ApplicationService applicationService;

    @PostMapping("/")
    public ApiResponse<JobCreateResponse> createJob(@RequestBody JobCreateRequest jobCreateRequest) {

        return ApiResponse.<JobCreateResponse>builder()
                .result(jobService.createJob(jobCreateRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<?>> getAllJobs(@RequestParam int pageNo,
                                   @RequestParam int pageSize,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String location,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(required = false) Type type,
                                   @RequestParam(required = false) BigDecimal minSalary,
                                   @RequestParam(required = false) BigDecimal maxSalary,
                                   @RequestParam(required = false) String userId) {
        return ApiResponse.<PageResponse<?>>builder()
                .result(jobService.getAllJobs(pageNo, pageSize, keyword, location, category, type, minSalary, maxSalary))
                .build();
    }

    @GetMapping("/getJobStatus")
    public ApiResponse<PageResponse<?>> getAllJobsStatus(@RequestParam int pageNo,
                                                   @RequestParam int pageSize,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String location,
                                                   @RequestParam(required = false) String category,
                                                   @RequestParam(required = false) Type type,
                                                   @RequestParam(required = false) BigDecimal minSalary,
                                                   @RequestParam(required = false) BigDecimal maxSalary,
                                                   @RequestParam(required = false) String userId) {
        return ApiResponse.<PageResponse<?>>builder()
                .result(jobService.getAllJobsStatus(pageNo, pageSize, keyword, location, category, type, minSalary, maxSalary))
                .build();
    }

    @PostMapping("/save-job")
    public ApiResponse<String> saveJob(@RequestParam String jobId) {
        savedJobService.saveJob(jobId);

        return ApiResponse.<String>builder()
                .result("Đã lưu")
                .build();
    }

    @PostMapping("/apply-job")
    public ApiResponse<String> applyJob(@RequestParam String jobId) {
        applicationService.createApplication(jobId);

        return ApiResponse.<String>builder()
                .result("Đã nộp đơn")
                .build();
    }
}
