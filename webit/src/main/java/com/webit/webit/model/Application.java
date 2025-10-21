package com.webit.webit.model;


import com.webit.webit.util.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "application")
public class Application extends AbstractClass{
    @Id
    private String applicationId;

    @DBRef
    private Job job;

    @DBRef
    private User applicant;

    private String resume;

    private Status status;
}
