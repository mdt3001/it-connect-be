package com.webit.webit.dto.response.job;


import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobInfoResponse {

    private String jobId;

    private String title;

    private String description;

    private String location;

    private String category;

    private Type type;

    private String companyName;

    private String userId;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private boolean isClosed;

}
