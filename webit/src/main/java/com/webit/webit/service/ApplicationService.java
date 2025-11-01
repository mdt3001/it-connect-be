package com.webit.webit.service;

import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.application.ApplicationResponse;
import com.webit.webit.dto.response.application.ApplicationResponseById;
import com.webit.webit.dto.response.application.ApplicationStatus;
import com.webit.webit.dto.response.application.MyApplicationResponse;
import com.webit.webit.util.Status;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(String jobId);

    List<MyApplicationResponse> getMyApplication();

    PageResponse<?> getApplicants(int pageNo, int pageSize, String jobId);

    ApplicationResponseById getApplication(String applicationId);

    Status updateStatus(String applicationId, ApplicationStatus status);
}
