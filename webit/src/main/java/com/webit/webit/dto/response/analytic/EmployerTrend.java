package com.webit.webit.dto.response.analytic;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerTrend {
    private double activeJobs;

    private double applications;

    private double hired;
}
