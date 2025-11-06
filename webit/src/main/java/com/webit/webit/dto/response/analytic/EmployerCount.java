package com.webit.webit.dto.response.analytic;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerCount {

    private long totalActiveJobs;

    private long totalApplications;

    private long totalHired;
}
