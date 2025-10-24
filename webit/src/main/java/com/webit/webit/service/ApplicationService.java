package com.webit.webit.service;

import com.webit.webit.dto.response.application.ApplicationResponse;

public interface ApplicationService {
    ApplicationResponse createApplication(String jobId);
}
