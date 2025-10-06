package com.webit.webit.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("jobseeker")
    JOBSEEKER,
    @JsonProperty("employer")
    EMPLOYER,
    @JsonProperty("admin")
    ADMIN
}
