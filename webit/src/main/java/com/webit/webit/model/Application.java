package com.webit.webit.model;


import com.webit.webit.util.Status;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "application")
@Builder
public class Application extends AbstractClass{
    @Id
    private String applicationId;

    private String job;

    private String applicant;

    private String resume;

    private Status status;
}
