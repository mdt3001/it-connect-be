package com.webit.webit.dto.response.application;


import com.webit.webit.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {

    private String applicationId;

    private String job;

    private String applicant;

    private String resume;

    private Status status;
}
