package com.webit.webit.controller;


import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.ApiResponse;

import com.webit.webit.dto.response.UserResponse;
import com.webit.webit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserDTORequest userDTO) {
        return ApiResponse.<UserResponse>builder()
                .message("Ok")
                .result(userService.savedUser(userDTO))
                .build();
    }
}
