package com.webit.webit.dto.response.application;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationCount {
    @Field("_id")
    private String jobId;// map từ "_id" trong aggregation result

    private long count;    // map từ "count"
}
