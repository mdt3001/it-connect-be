package com.webit.webit.service.impl;


import com.webit.webit.dto.request.job.JobInfoRequest;
import com.webit.webit.dto.response.application.ApplicationCount;
import com.webit.webit.dto.response.job.JobStatus;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobInfoResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.Application;
import com.webit.webit.model.Job;
import com.webit.webit.model.SavedJob;
import com.webit.webit.model.User;
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
                .requirement(jobInfoRequest.getRequirement())
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
                .requirement(job.getRequirement())
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
                        .requirement(allJob.getRequirement())
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

        long applicationCount = applicationRepository.countByJob(jobId);

        return JobInfoResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirement(job.getRequirement())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .companyName(job.getCompany().getName())
                .userId(job.getCompany().getUserId())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .applicationCount(applicationCount)
                .isClosed(job.isClosed())
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

        if (jobInfoRequest.getRequirement() != null) {
            job.setRequirement(jobInfoRequest.getRequirement());
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
                .requirement(job.getRequirement())
                .location(job.getLocation())
                .category(job.getCategory())
                .type(job.getType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .build();
    }

    @Override
    public PageResponse<?> getJobsEmployer(int pageNo, int pageSize, String search, String status, String sortField, String sortDirection) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        // Validate và map sortField
        String validSortField = "location".equalsIgnoreCase(sortField) ? "location" : "title";
        
        // Build sort direction
        org.springframework.data.domain.Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? org.springframework.data.domain.Sort.Direction.DESC
                : org.springframework.data.domain.Sort.Direction.ASC;

        // Create Pageable with sorting - lấy tất cả jobs trước để filter
        // Tạm thời không sort ở đây, sẽ sort sau khi filter
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        
        Page<Job> page = jobRepository.findAllByCompany_UserId(userId, pageable);
        
        // Filter by search keyword (title hoặc location)
        List<Job> filteredJobs = page.getContent();
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            filteredJobs = filteredJobs.stream()
                    .filter(job -> {
                        String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
                        String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";
                        return title.contains(searchLower) || location.contains(searchLower);
                    })
                    .collect(Collectors.toList());
        }

        // Filter by status
        if (status != null && !status.equalsIgnoreCase("All")) {
            if ("Active".equalsIgnoreCase(status)) {
                filteredJobs = filteredJobs.stream()
                        .filter(job -> !job.isClosed())
                        .collect(Collectors.toList());
            } else if ("Closed".equalsIgnoreCase(status)) {
                filteredJobs = filteredJobs.stream()
                        .filter(job -> job.isClosed())
                        .collect(Collectors.toList());
            }
        }

        // Sort filtered results
        if ("location".equalsIgnoreCase(validSortField)) {
            filteredJobs = filteredJobs.stream()
                    .sorted((j1, j2) -> {
                        String loc1 = j1.getLocation() != null ? j1.getLocation() : "";
                        String loc2 = j2.getLocation() != null ? j2.getLocation() : "";
                        int compare = loc1.compareToIgnoreCase(loc2);
                        return direction == org.springframework.data.domain.Sort.Direction.ASC ? compare : -compare;
                    })
                    .collect(Collectors.toList());
        } else { // sort by title (default)
            filteredJobs = filteredJobs.stream()
                    .sorted((j1, j2) -> {
                        String title1 = j1.getTitle() != null ? j1.getTitle() : "";
                        String title2 = j2.getTitle() != null ? j2.getTitle() : "";
                        int compare = title1.compareToIgnoreCase(title2);
                        return direction == org.springframework.data.domain.Sort.Direction.ASC ? compare : -compare;
                    })
                    .collect(Collectors.toList());
        }

        // Apply pagination manually
        int totalItems = filteredJobs.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int start = pageNo * pageSize;
        int end = Math.min(start + pageSize, totalItems);
        
        List<Job> paginatedJobs = start < totalItems ? filteredJobs.subList(start, end) : new ArrayList<>();

        List<String> jobIds = paginatedJobs.stream()
                .map(Job::getJobId)
                .collect(Collectors.toList());

        Map<String, Long> applicationCounts = jobIds.isEmpty() 
                ? Map.of()
                : applicationRepository.countApplicationsByJobIds(jobIds).stream()
                        .collect(Collectors.toMap(ApplicationCount::getJobId, ApplicationCount::getCount));

        List<JobInfoResponse> response = paginatedJobs.stream().map(job -> {
            User company = job.getCompany();
            return JobInfoResponse.builder()
                    .jobId(job.getJobId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .requirement(job.getRequirement())
                    .location(job.getLocation())
                    .category(job.getCategory())
                    .type(job.getType())
                    .companyName(company != null ? company.getName() : null)
                    .userId(company != null ? company.getUserId() : null)
                    .salaryMin(job.getSalaryMin())
                    .salaryMax(job.getSalaryMax())
                    .isClosed(job.isClosed())
                    .applicationCount(applicationCounts.getOrDefault(job.getJobId(), 0L))
                    .build();
        }).collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(response)
                .totalPage(totalPages)
                .build();
    }

    @Override
    public void toogleClose(String jobId) {
        var job = jobRepository.findByJobId(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_EXISTED));

        job.setClosed(true);

        jobRepository.save(job);
    }
}
