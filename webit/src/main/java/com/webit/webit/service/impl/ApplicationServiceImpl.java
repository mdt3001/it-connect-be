package com.webit.webit.service.impl;

import com.webit.webit.dto.response.application.ApplicationResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.Application;
import com.webit.webit.repository.ApplicationRepository;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.util.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationResponse createApplication(String jobId) {
        if (!StringUtils.hasLength(jobId)) {
            throw new AppException(ErrorCode.UNSAVED);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        Application application = Application.builder()
                .applicant(userId)
                .job(jobId)
                .resume("url")
                .status(Status.APPLIED)
                .build();
        applicationRepository.save(application);

        return ApplicationResponse.builder()
                .applicationId(application.getApplicationId())
                .applicant(application.getApplicant())
                .job(application.getJob())
                .resume(application.getResume())
                .status(application.getStatus())
                .build();
    }
}
