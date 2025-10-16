package com.webit.webit.service.impl;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.request.UserUpdateRequest;
import com.webit.webit.dto.response.UserDetailResponse;
import com.webit.webit.dto.response.UserResponse;
import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.mapper.UserMapper;
import com.webit.webit.model.User;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDetailResponse getUser(String userId) {

        var user = userRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return UserDetailResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .resume(user.getResume())
                .companyName(user.getCompanyName())
                .companyLogo(user.getCompanyLogo())
                .companyDescription(user.getCompanyDescription())
                .build();
    }

    @Override
    public UserDetailResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {

        var user = userRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (userUpdateRequest.getName() != null) {
            user.setName(userUpdateRequest.getName());
        }

        if (userUpdateRequest.getAvatar() != null) {
            user.setAvatar(userUpdateRequest.getAvatar());
        }

        if (userUpdateRequest.getResume() != null) {
            user.setResume(userUpdateRequest.getResume());
        }

        if (userUpdateRequest.getCompanyName() != null) {
            user.setCompanyName(userUpdateRequest.getCompanyName());
        }

        if (userUpdateRequest.getCompanyDescription() != null) {
            user.setCompanyDescription(userUpdateRequest.getCompanyDescription());
        }

        if (userUpdateRequest.getCompanyLogo()!= null) {
            user.setCompanyLogo(userUpdateRequest.getCompanyLogo());
        }

        userRepository.save(user);


        return UserDetailResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .avatar(user.getAvatar())
                .resume(user.getResume())
                .companyName(user.getCompanyName())
                .companyDescription(user.getCompanyDescription())
                .companyLogo(user.getCompanyLogo())
                .build();
    }

}
