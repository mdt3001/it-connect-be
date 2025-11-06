package com.webit.webit.dto.response.analytic.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationSummary {
    private String applicantName;

    private String applicantEmail;

    private String jobTitle;

    private LocalDateTime createdAt;
}
