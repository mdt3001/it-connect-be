package com.webit.webit.controller;


import com.webit.webit.dto.request.job.JobInfoRequest;
import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobInfoResponse;
import com.webit.webit.repository.JobRepository;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.service.JobService;
import com.webit.webit.service.SavedJobService;
import com.webit.webit.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private final JobRepository jobRepository;


    @PostMapping("/")
    public ApiResponse<JobInfoResponse> createJob(@RequestBody JobInfoRequest jobInfoRequest) {

        return ApiResponse.<JobInfoResponse>builder()
                .result(jobService.createJob(jobInfoRequest))
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

    // status: (saved, ...)
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

    @GetMapping("/{jobId}")
    public ApiResponse<JobInfoResponse> getJob(@PathVariable String jobId) {
        return ApiResponse.<JobInfoResponse>builder()
                .result(jobService.getJob(jobId))
                .build();
    }

    @DeleteMapping("/{jobId}")
    public ApiResponse<String> deleteJob(@PathVariable String jobId) {
        jobRepository.deleteById(jobId);
        return ApiResponse.<String>builder().result("Công việc đã xóa").build();
    }

    @PutMapping("/{jobId}")
    public ApiResponse<JobInfoResponse> updateJob(@PathVariable String jobId, @RequestBody JobInfoRequest jobInfoRequest) {
        return ApiResponse.<JobInfoResponse>builder()
                .result(jobService.updateJob(jobId, jobInfoRequest))
                .build();
    }


    @GetMapping("get-jobs-employer")
    public ApiResponse<PageResponse<?>> getJobsEmployer(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "All") String status,
            @RequestParam(required = false, defaultValue = "title") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        return ApiResponse.<PageResponse<?>>builder()
                .result(jobService.getJobsEmployer(pageNo, pageSize, search, status, sortField, sortDirection))
                .build();
    }

    @PatchMapping("/{jobId}/toogle-close")
    public ApiResponse<String> toogleClose(@PathVariable String jobId) {
        jobService.toogleClose(jobId);

        return ApiResponse.<String>builder()
                .message("Đã đóng việc")
                .build();
    }

}
