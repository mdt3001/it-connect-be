package com.webit.webit.model;

import com.webit.webit.util.Type;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "job")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job extends AbstractClass {
    @Id
    private String jobId;

    private String title;

    private String description;

    private String location;

    private String category;

    private Type type;

    @DBRef
    private User company;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private boolean isClosed = false;
}
