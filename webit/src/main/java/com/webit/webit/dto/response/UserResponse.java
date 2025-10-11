package com.webit.webit.dto.response;

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

}
