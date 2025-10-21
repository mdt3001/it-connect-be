package com.webit.webit.dto.response.job;


import com.webit.webit.model.User;
import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCreateResponse {

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

}
