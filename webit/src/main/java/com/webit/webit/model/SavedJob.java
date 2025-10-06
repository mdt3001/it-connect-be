package com.webit.webit.model;


import com.webit.webit.util.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "saved_job")
public class SavedJob extends AbstractClass{
    @Id
    private UUID savedJobId = UUID.randomUUID();

    @DBRef
    private Job job;

    @DBRef
    private User applicant;

    private String resume;

    private Status status;
}
