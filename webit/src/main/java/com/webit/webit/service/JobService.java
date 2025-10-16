package com.webit.webit.service;


import com.webit.webit.dto.request.job.JobCreateRequest;
import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.job.JobCreateResponse;
import com.webit.webit.util.Type;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface JobService {

    JobCreateResponse createJob(JobCreateRequest jobCreateRequest);

    PageResponse<?> getAllJobs(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary);
}
