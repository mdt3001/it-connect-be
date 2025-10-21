package com.webit.webit.mapper;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.request.UserUpdateRequest;
import com.webit.webit.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserDTORequest toDTO(final User user) {

        return UserDTORequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .resume(user.getResume())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .companyDescription(user.getCompanyDescription())
                .companyName(user.getCompanyName())
                .companyLogo(user.getCompanyLogo())
                .build();
    }

    public User toEntity(final UserDTORequest userDTO) {

        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .resume(userDTO.getResume())
                .password(userDTO.getPassword())
                .avatar(userDTO.getAvatar())
                .companyDescription(userDTO.getCompanyDescription())
                .companyName(userDTO.getCompanyName())
                .companyLogo(userDTO.getCompanyLogo())
                .build();
    }


}
