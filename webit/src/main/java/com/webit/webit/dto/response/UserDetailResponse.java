package com.webit.webit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {

    private String userId;

    private String name;

    private String email;



    private String avatar;

    private String resume;

    private String companyName;

    private String companyDescription;

    private String companyLogo;
}
