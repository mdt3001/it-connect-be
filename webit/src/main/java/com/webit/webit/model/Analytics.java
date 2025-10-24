package com.webit.webit.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "analytics")
public class Analytics extends AbstractClass{
    @Id
    private String analyticId;
    
    @DBRef
    private User employer;

    private int totalJobsPosted;

    private int totalHired;
    
}
