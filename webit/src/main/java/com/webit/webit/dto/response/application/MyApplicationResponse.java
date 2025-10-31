package com.webit.webit.dto.response.application;


import com.webit.webit.util.Status;
import com.webit.webit.util.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyApplicationResponse {

    private String applicationId;

    private String job;

    private String title;

    private String location;

    private Type type;

    private String company; // userId của người tuyển cái job này

    private String applicant;

    private String resume;

    private Status status;
}
