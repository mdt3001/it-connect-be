package com.webit.webit.service;

import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.SaveJob.SaveJobResponse;

public interface SavedJobService {
    SaveJobResponse saveJob(String jobId);

    String unsaveJob(String jobId);

    PageResponse<?> getJobSaved(int pageNo, int pageSize);

}
