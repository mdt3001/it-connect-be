package com.webit.webit.service;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse savedUser(UserDTORequest userDTO);
}
