package com.webit.webit.dto.response.analytic;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerAnalytics {
    private EmployerCount counts;

    private EmployerTrend trends;

    private EmployerRecentData data;
}
