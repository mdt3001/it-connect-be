package com.webit.webit.dto.response;

import com.webit.webit.util.Role;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String userId;

    private String name;

    private String email;

    private String token;

    private Role role;

    private String avatar;

    private String resume;

    private String companyName;

    private String companyDescription;

    private String companyLogo;

}
