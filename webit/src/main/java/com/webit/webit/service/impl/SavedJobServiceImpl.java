package com.webit.webit.service.impl;

import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.SavedJob;
import com.webit.webit.repository.SavedJobRepository;
import com.webit.webit.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    public void saveJob(String jobId) {

        if (!StringUtils.hasLength(jobId)) {
            throw new AppException(ErrorCode.UNSAVED);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();


        SavedJob savedJob = SavedJob.builder()
                .jobseeker(userId)
                .job(jobId)
                .build();
        savedJobRepository.save(savedJob);
    }
}
