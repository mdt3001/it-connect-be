package com.webit.webit.dto.request.job;


import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobInfoRequest {

    private String title;

    private String description;

    private String requirement;

    private String location;

    private String category;

    private Type type;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

}
