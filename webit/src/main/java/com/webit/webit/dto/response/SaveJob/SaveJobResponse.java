package com.webit.webit.dto.response.SaveJob;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveJobResponse {
    private String saveJobId;

    private String jobseeker;

    private String job;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
