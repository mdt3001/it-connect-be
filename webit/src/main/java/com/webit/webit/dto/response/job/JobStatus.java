package com.webit.webit.dto.response.job;

import com.webit.webit.util.Status;
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
public class JobStatus {

    private String jobId;

    private String title;

    private String description;

    private String requirement;

    private String location;

    private String category;

    private Type type;

    private String companyName;

    private String companyLogo;

    private String userId;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private long applicationCount;

    private boolean isSaved;

    private boolean isApplied;

    private Status status;

    private boolean isClosed;

}


