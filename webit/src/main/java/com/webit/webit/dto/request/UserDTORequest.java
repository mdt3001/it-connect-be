package com.webit.webit.dto.request;

import com.webit.webit.util.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTORequest {

    private String name;

    @NonNull
    private String email;

    @NonNull
    @NotBlank()
    private String password;

    @NonNull
    private Role role;

    private String avatar;

    private String resume;

    private String companyName;

    private String companyDescription;

    private String companyLogo;
}
