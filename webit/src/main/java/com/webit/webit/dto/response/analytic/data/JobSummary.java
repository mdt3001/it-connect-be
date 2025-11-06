package com.webit.webit.dto.response.analytic.data;


import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSummary {
    private String title;

    private String location;

    private Type type;

    private boolean isClosed;

    private LocalDateTime createdAt;
}
