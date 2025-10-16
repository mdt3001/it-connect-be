package com.webit.webit.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    private String name;

    private String avatar;

    private String resume;

    private String companyName;

    private String companyDescription;

    private String companyLogo;
}
