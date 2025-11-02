package com.webit.webit.repository;

import com.webit.webit.dto.response.application.ApplicationCount;
import com.webit.webit.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAllByApplicant(String userId);

    Page<Application> findAllByJob(String jobId, Pageable pageable);

    @Aggregation(pipeline = { "{ $match: { job: { $in: ?0 } } }", "{ $group: { _id: '$job', count: { $sum: 1 } } }" })
    List<ApplicationCount> countApplicationsByJobIds(List<String> jobIds);
}
