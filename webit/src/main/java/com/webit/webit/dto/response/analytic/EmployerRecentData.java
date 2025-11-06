package com.webit.webit.dto.response.analytic;


import com.webit.webit.dto.response.analytic.data.ApplicationSummary;
import com.webit.webit.dto.response.analytic.data.JobSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerRecentData {
    private List<JobSummary> recentJobs;

    private List<ApplicationSummary> recentApplications;
}
