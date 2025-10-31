package com.webit.webit.dto.response.application;


import com.webit.webit.util.Status;
import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponseById {
    private String applicationId;

    private String job;

    private String title;

    private String applicant;

    private String nameApplicant;

    private String email;

    private String avatar;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
