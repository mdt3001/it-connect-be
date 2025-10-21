package com.webit.webit.model;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;


@Data
public abstract class AbstractClass {

    @CreatedDate
    @Field("createdAt")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private OffsetDateTime updatedAt;
}
