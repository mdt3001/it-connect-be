package com.webit.webit.service.impl;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.UserResponse;
import com.webit.webit.mapper.UserMapper;
import com.webit.webit.model.User;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse savedUser(UserDTORequest userDTO) {

        User user = userMapper.toEntity(userDTO);

        user.setUserId(UUID.randomUUID());

        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
