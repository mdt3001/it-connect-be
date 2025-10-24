package com.webit.webit.service;


import com.webit.webit.dto.request.job.JobInfoRequest;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobInfoResponse;
import com.webit.webit.util.Type;

import java.math.BigDecimal;


public interface JobService {

    JobInfoResponse createJob(JobInfoRequest jobInfoRequest);

    PageResponse<?> getAllJobs(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary);

    PageResponse<?> getAllJobsStatus(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary);

    JobInfoResponse getJob(String jobId);

    void deleteJob(String jobId);

    JobInfoResponse updateJob(String jobId, JobInfoRequest jobInfoRequest);

    PageResponse<?> getJobsEmployer(int pageNo, int pageSize);

    void toogleClose(String jobId);
}
