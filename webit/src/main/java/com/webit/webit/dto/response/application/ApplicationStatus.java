package com.webit.webit.dto.response.application;


import com.webit.webit.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationStatus {
    private Status status;
}
