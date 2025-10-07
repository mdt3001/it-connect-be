package com.webit.webit.service;


import com.webit.webit.dto.request.AuthenticationRequest;
import com.webit.webit.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
