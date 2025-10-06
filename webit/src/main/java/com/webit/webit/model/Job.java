package com.webit.webit.model;

import com.webit.webit.util.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "job")
public class Job extends AbstractClass {
    @Id
    private UUID jobId = UUID.randomUUID();

    private String title;

    private String description;

    private String location;

    private String category;

    private Type type;

    @DBRef
    private User company;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

}
