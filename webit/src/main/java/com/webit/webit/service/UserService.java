package com.webit.webit.service;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.request.UserUpdateRequest;
import com.webit.webit.dto.response.UserDetailResponse;
import com.webit.webit.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {

    void deleteUser(String userId);

    UserDetailResponse getUser(String userId);

    UserDetailResponse updateUser(String userId, UserUpdateRequest userUpdateRequest);
}
