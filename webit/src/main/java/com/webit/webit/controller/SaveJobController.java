package com.webit.webit.controller;


import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.SaveJob.SaveJobResponse;
import com.webit.webit.repository.SavedJobRepository;
import com.webit.webit.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/save-jobs")
public class SaveJobController {

   private final SavedJobService savedJobService;

    @PostMapping("{jobId}")
    public ApiResponse<SaveJobResponse> saveJob(@PathVariable String jobId) {

        return ApiResponse.<SaveJobResponse>builder()
                .result( savedJobService.saveJob(jobId))
                .build();
    }

    @DeleteMapping("{jobId}")
    public ApiResponse<String> unsaveJob(@PathVariable String jobId) {

        return ApiResponse.<String>builder()
                .result(savedJobService.unsaveJob(jobId)).build();
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<?>> getJobSaved(@RequestParam int pageNo, @RequestParam int pageSize) {
        return ApiResponse.<PageResponse<?>>builder()
                .result(savedJobService.getJobSaved(pageNo, pageSize))
                .build();
    }
}
