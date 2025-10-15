package com.webit.webit.controller;


import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.ApiResponse;

import com.webit.webit.dto.response.UserDetailResponse;
import com.webit.webit.dto.response.UserResponse;
import com.webit.webit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @GetMapping("{userId}")
    public ApiResponse<UserDetailResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Get User")
                .result(userService.getUser(userId))
                .build();
    }
}
