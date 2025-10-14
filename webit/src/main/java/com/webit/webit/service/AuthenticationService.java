package com.webit.webit.service;


import com.nimbusds.jose.JOSEException;
import com.webit.webit.dto.request.AuthenticationRequest;
import com.webit.webit.dto.request.IntrospectRequest;
import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.dto.response.AuthenticationResponse;
import com.webit.webit.dto.response.ImageResponse;
import com.webit.webit.dto.response.IntrospectResponse;
import com.webit.webit.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

public interface AuthenticationService {

    UserResponse authenticate(AuthenticationRequest authenticationRequest);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    UserResponse register(UserDTORequest userDTO);

    ImageResponse uploadImage(MultipartFile file);

}
