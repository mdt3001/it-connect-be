package com.webit.webit.model;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "saved_job")
@Builder
public class SavedJob extends AbstractClass{
    @Id
    private String savedJobId;


    private String jobseeker;


    private String job;
}
