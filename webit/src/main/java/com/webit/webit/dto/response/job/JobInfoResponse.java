package com.webit.webit.dto.response.job;


import com.webit.webit.util.Status;
import com.webit.webit.util.Type;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class JobInfoResponse {

    private String jobId;

    private String title;

    private String description;

    private String requirement;

    private String location;

    private String category;

    private Type type;

    private String companyName;

    private String userId;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private boolean isClosed;

    private Status status;

    private long applicationCount;

}
