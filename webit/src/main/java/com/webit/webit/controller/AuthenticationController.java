package com.webit.webit.controller;


import com.nimbusds.jose.JOSEException;
import com.webit.webit.dto.request.AuthenticationRequest;
import com.webit.webit.dto.request.IntrospectRequest;
import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.*;
import com.webit.webit.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
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
    public ApiResponse<UserResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<UserResponse>builder()
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

    @PostMapping("/upload-image")
    public ApiResponse<ImageResponse> uploadImage(@RequestParam MultipartFile file) {
        var response = authenticationService.uploadImage(file);
        return ApiResponse.<ImageResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/upload-cv")
    public ApiResponse<CVResponse> uploadCV(@RequestParam MultipartFile file){
        var response = authenticationService.uploadCV(file);
        return ApiResponse.<CVResponse>builder()
                .result(response)
                .build();
    }
}
