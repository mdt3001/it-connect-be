package com.webit.webit.service.impl;

import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.SaveJob.SaveJobResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.Job;
import com.webit.webit.model.SavedJob;
import com.webit.webit.repository.JobRepository;
import com.webit.webit.repository.SavedJobRepository;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    private final JobRepository jobRepository;

    @Override
    public SaveJobResponse saveJob(String jobId) {

        if (!StringUtils.hasLength(jobId)) {
            throw new AppException(ErrorCode.UNSAVED);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        if (savedJobRepository.existsByJobseekerAndJob(userId, jobId)) {
            throw new AppException(ErrorCode.SAVE_JOB_EXISTED);
        }

        SavedJob savedJob = SavedJob.builder()
                .jobseeker(userId)
                .job(jobId)
                .build();
        savedJobRepository.save(savedJob);

        return SaveJobResponse.builder()
                .saveJobId(savedJob.getSavedJobId())
                .jobseeker(savedJob.getJobseeker())
                .job(savedJob.getJob())
                .createdAt(savedJob.getCreatedAt())
                .updatedAt(savedJob.getUpdatedAt())
                .build();
    }

    @Override
    public String unsaveJob(String jobId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

       if (!savedJobRepository.existsByJobseekerAndJob(userId, jobId)) {
           return "Công việc này chưa được lưu";
       }

       savedJobRepository.deleteByJobseekerAndJob(userId, jobId);

       return "Bỏ lưu";

    }

    @Override
    public PageResponse<?> getJobSaved(int pageNo, int pageSize) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<SavedJob> page = savedJobRepository.findAllByJobseeker(userId, pageable);

        List<String> jobIds = page.getContent().stream().map(SavedJob::getJob).toList();

        List<Job> jobs = jobRepository.findByJobIdIn(jobIds);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(jobs)
                .build();
    }
}
