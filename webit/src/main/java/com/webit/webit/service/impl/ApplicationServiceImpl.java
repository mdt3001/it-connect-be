package com.webit.webit.service.impl;

import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.dto.response.application.ApplicationResponse;
import com.webit.webit.dto.response.application.ApplicationResponseById;
import com.webit.webit.dto.response.application.MyApplicationResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.Application;
import com.webit.webit.model.Job;
import com.webit.webit.model.User;
import com.webit.webit.repository.ApplicationRepository;
import com.webit.webit.repository.JobRepository;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.ApplicationService;
import com.webit.webit.util.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

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

    @Override
    public List<MyApplicationResponse> getMyApplication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        List<Application> applications = applicationRepository.findAllByApplicant(userId);

        if (applications.isEmpty()) {
            throw new AppException(ErrorCode.NOTHING);
        }

        List<String> jobIds = applications.stream().map(Application::getJob).toList();

        List<Job> jobs = jobRepository.findByJobIdIn(jobIds);

        Map<String, Job> mapJob = jobs.stream().collect(Collectors.toMap(Job::getJobId, job -> job));

        List<MyApplicationResponse> myApplicationResponses = new ArrayList<>();

        applications.forEach(application -> {
            Job job = mapJob.get(application.getJob());

            myApplicationResponses.add(MyApplicationResponse.builder()
                    .applicationId(application.getApplicationId())
                    .job(application.getJob())
                    .title(job.getTitle())
                    .location(job.getLocation())
                    .type(job.getType())
                    .company(job.getCompany().getCompanyName())
                    .applicant(userId)
                    .resume(application.getResume())
                    .status(application.getStatus())
                    .build());

        });

        return myApplicationResponses;
    }

    @Override
    public ApplicationResponseById getApplication(String applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new AppException(ErrorCode.NOTHING));

        Job job = jobRepository.findByJobId(application.getJob()).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_EXISTED));

        User user = userRepository.findByUserId(application.getApplicant()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));



        return ApplicationResponseById.builder()
                .applicationId(application.getApplicationId())
                .job(job.getJobId())
                .title(job.getTitle())
                .applicant(user.getUserId())
                .nameApplicant(user.getName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    @Override
    public String updateStatus(String applicationId, Status status) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new AppException(ErrorCode.NOTHING));

        application.setStatus(status);

        applicationRepository.save(application);

        return status.toString();
    }

    @Override
    public PageResponse<?> getApplicants(int pageNo, int pageSize, String jobId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Application> applications = applicationRepository.findAllByJob(jobId, pageable);

        //xử lý user
        List<Application> applicants = applications.getContent();

        List<String> userIds = new ArrayList<>();

        applicants.forEach(applicant -> userIds.add(applicant.getApplicant()));

        List<User> users = userRepository.findAllByUserIdIn(userIds);

        Map<String, User> mapUser = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));

        List<ApplicationResponseById> response = new ArrayList<>();

        //xử lý job
        Job job = jobRepository.findByJobId(jobId).orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_EXISTED));

        applicants.forEach(applicant -> {
            User user = mapUser.get(applicant.getApplicant());
            response.add(ApplicationResponseById.builder()
                            .applicationId(applicant.getApplicationId())
                            .job(applicant.getJob())
                            .title(job.getTitle())
                            .applicant(applicant.getApplicant())
                            .nameApplicant(user.getName())
                            .email(user.getEmail())
                            .avatar(user.getAvatar())
                            .status(applicant.getStatus())
                            .createdAt(applicant.getCreatedAt())
                            .updatedAt(applicant.getUpdatedAt())
                    .build());
        } );

        return PageResponse.builder().pageNo(pageNo).pageSize(pageSize).totalPage(applications.getTotalPages()).items(response).build();
    }
}
