package com.webit.webit.controller;


import com.nimbusds.jose.JOSEException;
import com.webit.webit.dto.request.AuthenticationRequest;
import com.webit.webit.dto.request.IntrospectRequest;
import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.dto.response.AuthenticationResponse;
import com.webit.webit.dto.response.IntrospectResponse;
import com.webit.webit.dto.response.UserResponse;
import com.webit.webit.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserDTORequest userDTO) {
        return ApiResponse.<UserResponse>builder()
                .result(authenticationService.register(userDTO))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

}
