package com.webit.webit.service.impl;


import com.webit.webit.dto.response.analytic.EmployerAnalytics;
import com.webit.webit.dto.response.analytic.EmployerCount;
import com.webit.webit.dto.response.analytic.EmployerRecentData;
import com.webit.webit.dto.response.analytic.EmployerTrend;
import com.webit.webit.dto.response.analytic.data.ApplicationSummary;
import com.webit.webit.dto.response.analytic.data.JobSummary;
import com.webit.webit.model.Application;
import com.webit.webit.model.Job;
import com.webit.webit.model.User;
import com.webit.webit.repository.ApplicationRepository;
import com.webit.webit.repository.JobRepository;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.AnalyticsService;
import com.webit.webit.util.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final JobRepository jobRepository;

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    @Override
    public EmployerAnalytics getAnalytics() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String userId = jwt.getSubject();

        List<Job> jobList = jobRepository.findAllByCompany_UserId( userId);

        List<String> jobIds = jobList.stream().map(Job::getJobId).toList();

        long totalActiveJobs = jobRepository.countByCompany_UserIdAndIsClosedFalse(userId);

        long totalApplications = applicationRepository.countByJobIn(jobIds);

        long totalHired = applicationRepository.countByJobAndStatus(jobIds, Status.ACCEPTED);

        List<JobSummary> jobSummaries = jobRepository.findTop5ByCompany_UserIdOrderByCreatedAtDesc(userId).stream().map(job -> JobSummary.builder()
                .title(job.getTitle())
                .location(job.getLocation())
                .type(job.getType())
                .isClosed(job.isClosed())
                .createdAt(job.getCreatedAt())
                .build()).toList();

        List<Application> applications = applicationRepository.findTop5ByJobInOrderByCreatedAtDesc(jobIds);

        List<String> applicantIds = applications.stream()
                .map(Application::getApplicant)
                .distinct()
                .toList();

        List<String> jobIdList = applications.stream()
                .map(Application::getJob)
                .distinct()
                .toList();

        Map<String, User> userMap = userRepository.findByUserIdIn(applicantIds).stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        Map<String, Job> jobMap = jobRepository.findByJobIdIn(jobIdList).stream()
                .collect(Collectors.toMap(Job::getJobId, j -> j));

        List<ApplicationSummary> applicationSummaries = applications.stream()
                .map(app -> {
                    User user = userMap.get(app.getApplicant());
                    Job job = jobMap.get(app.getJob());
                    return ApplicationSummary.builder()
                            .applicantName(user != null ? user.getName() : "Unknown")
                            .applicantEmail(user != null ? user.getEmail() : "Unknown")
                            .jobTitle(job != null ? job.getTitle() : "Unknown")
                            .createdAt(app.getCreatedAt())
                            .build();
                })
                .toList();

        return EmployerAnalytics.builder()
                .counts(EmployerCount.builder().totalActiveJobs(totalActiveJobs).totalApplications(totalApplications).totalHired(totalHired).build())
                .trends(EmployerTrend.builder().activeJobs(getActiveJobTrend(userId)).applications(getApplicationTrend(userId, jobIds)).hired(getHiredTrend(userId, jobIds)).build())
                .data(EmployerRecentData.builder().recentJobs(jobSummaries).recentApplications(applicationSummaries).build())
                .build();
    }

    private double calculateTrend(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        double result = ((double) (current - previous) / previous) * 100.0;
        return Math.round(result * 100.0) / 100.0; // làm tròn 2 chữ số thập phân
    }

    private double getActiveJobTrend(String userId) {
        // Lấy dữ liệu 7 ngày gần nhất
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last7Days = now.minusDays(7);
        LocalDateTime prev7Days = now.minusDays(14);

        long current = jobRepository.countByCompany_UserIdAndCreatedAtBetween(userId, last7Days, now);
        long previous = jobRepository.countByCompany_UserIdAndCreatedAtBetween(userId, prev7Days, last7Days);

        return calculateTrend(current, previous);
    }

    private double getApplicationTrend(String userId, List<String> jobIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last7Days = now.minusDays(7);
        LocalDateTime prev7Days = now.minusDays(14);

        long current = applicationRepository.countByJobInAndCreatedAtBetween(jobIds, last7Days, now);
        long previous = applicationRepository.countByJobInAndCreatedAtBetween(jobIds, prev7Days, last7Days);

        return calculateTrend(current, previous);
    }

    private double getHiredTrend(String userId, List<String> jobIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last7Days = now.minusDays(7);
        LocalDateTime prev7Days = now.minusDays(14);

        long current = applicationRepository.countByJobInAndStatusAndCreatedAtBetween(jobIds, Status.ACCEPTED, last7Days, now);
        long previous = applicationRepository.countByJobInAndStatusAndCreatedAtBetween(jobIds, Status.ACCEPTED, prev7Days, last7Days);

        return calculateTrend(current, previous);
    }

}
