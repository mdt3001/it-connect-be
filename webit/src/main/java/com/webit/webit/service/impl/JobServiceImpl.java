package com.webit.webit.service.impl;


import com.webit.webit.dto.request.job.JobInfoRequest;
import com.webit.webit.dto.response.job.JobStatus;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobInfoResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.Application;
import com.webit.webit.model.Job;
import com.webit.webit.model.SavedJob;
import com.webit.webit.repository.ApplicationRepository;
import com.webit.webit.repository.JobRepository;
import com.webit.webit.repository.SavedJobRepository;
import com.webit.webit.repository.SearchRepository.SearchAllJobRepository;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.JobService;
import com.webit.webit.util.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final SavedJobRepository savedJobRepository;

    private final ApplicationRepository applicationRepository;

    private final SearchAllJobRepository searchAllJobRepository;


    @Override
    public JobInfoResponse createJob(JobInfoRequest jobInfoRequest) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        var user = userRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Job job = Job.builder()
                .title(jobInfoRequest.getTitle())
                .description(jobInfoRequest.getDescription())
                .location(jobInfoRequest.getLocation())
                .company(user)
                .category(jobInfoRequest.getCategory())
                .type(jobInfoRequest.getType())
                .salaryMin(jobInfoRequest.getSalaryMin())
                .salaryMax(jobInfoRequest.getSalaryMax())
                .build();

        jobRepository.save(job);

        return JobInfoResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .companyName(user.getCompanyName())
                .userId(user.getUserId())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .build();
    }

    @Override
    public PageResponse<?> getAllJobs(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary) {

        return searchAllJobRepository.getAllJobs(pageNo, pageSize, keyword, location, category, type, minSalary, maxSalary);
    }

    @Override
    public PageResponse<?> getAllJobsStatus(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary) {

        List<JobStatus> jobStatus = new ArrayList<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        var jobPage = searchAllJobRepository.getAllJobs(pageNo, pageSize, keyword, location, category, type, minSalary, maxSalary);

        List<JobInfoResponse> allJobs = (List<JobInfoResponse>) jobPage.getItems();

        List<SavedJob> savedJobsByUserId =  savedJobRepository.findAllByJobseeker(userId);

        List<Application> applyJobsByUserId = applicationRepository.findAllByApplicant(userId);

        Map<String, String> savejobMap = savedJobsByUserId.stream()
                .collect(Collectors.toMap(
                        s -> s.getJob(),        // key
                        s -> s.getJobseeker()  // value
                ));
        Map<String, String> applyjobMap = applyJobsByUserId.stream()
                .collect(Collectors.toMap(
                        s -> s.getJob(),        // key
                        s -> s.getApplicant()  // value
                ));

        allJobs.forEach(allJob -> {
            boolean saved = savejobMap.containsKey(allJob.getJobId());
            boolean applied = applyjobMap.containsKey(allJob.getJobId());
                jobStatus.add(JobStatus.builder()
                        .jobId(allJob.getJobId())
                        .title(allJob.getTitle())
                        .description(allJob.getDescription())
                        .location(allJob.getLocation())
                        .category(allJob.getCategory())
                        .type(allJob.getType())
                        .companyName(allJob.getCompanyName())
                        .userId(allJob.getUserId())
                        .salaryMin(allJob.getSalaryMin())
                        .salaryMax(allJob.getSalaryMax())
                        .isSaved(saved)
                        .isApplied(applied)
                        .build());
        });

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(jobPage.getTotalPage())
                .items(jobStatus)
                .build();
    }

    @Override
    public JobInfoResponse getJob(String jobId) {

        var job = jobRepository.findByJobId(jobId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return JobInfoResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .companyName(job.getCompany().getName())
                .userId(job.getCompany().getUserId())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .build();
    }

    @Override
    public void deleteJob(String jobId) {
        jobRepository.deleteById(jobId);
    }

    @Override
    public JobInfoResponse updateJob(String jobId, JobInfoRequest jobInfoRequest) {

        var job = jobRepository.findByJobId(jobId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(jobInfoRequest.getTitle() != null) {
            job.setTitle(jobInfoRequest.getTitle());
        }

        if(jobInfoRequest.getDescription() != null) {
            job.setDescription(jobInfoRequest.getDescription());
        }

        if(jobInfoRequest.getLocation() != null) {
            job.setLocation(jobInfoRequest.getLocation());
        }

        if(jobInfoRequest.getCategory() != null) {
            job.setCategory(jobInfoRequest.getCategory());
        }

        if(jobInfoRequest.getType() != null) {
            job.setType(jobInfoRequest.getType());
        }

        if(jobInfoRequest.getSalaryMin() != null) {
            job.setSalaryMin(jobInfoRequest.getSalaryMin());
        }

        if(jobInfoRequest.getSalaryMax() != null) {
            job.setSalaryMax(jobInfoRequest.getSalaryMax());
        }

        jobRepository.save(job);

        return JobInfoResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .build();
    }
    @Override
    public PageResponse<?> getJobsEmployer(int pageNo, int pageSize) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        Pageable pageable =  PageRequest.of(pageNo, pageSize);

        Page<Job> page = jobRepository.findAllByCompany_UserId(userId, pageable);

        List<JobInfoResponse> response = page.getContent().stream().map(job -> JobInfoResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .companyName(job.getCompany().getName())
                .userId(job.getCompany().getUserId())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .build()).toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(response)
                .totalPage(page.getTotalPages())
                .build();
    }
}
