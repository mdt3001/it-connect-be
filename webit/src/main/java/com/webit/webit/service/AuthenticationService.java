package com.webit.webit.service;


import com.nimbusds.jose.JOSEException;
import com.webit.webit.dto.request.AuthenticationRequest;
import com.webit.webit.dto.request.IntrospectRequest;
import com.webit.webit.dto.response.AuthenticationResponse;
import com.webit.webit.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}
