package com.webit.webit.repository;

import com.webit.webit.dto.response.application.ApplicationCount;
import com.webit.webit.model.Application;
import com.webit.webit.util.Status;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAllByApplicant(String userId);

    Page<Application> findAllByJob(String jobId, Pageable pageable);

    @Aggregation(pipeline = { "{ $match: { job: { $in: ?0 } } }", "{ $group: { _id: '$job', count: { $sum: 1 } } }" })
    List<ApplicationCount> countApplicationsByJobIds(List<String> jobIds);

    long countByJob(String jobId);

    long countByJobIn(List<String> jobId);

    long countByJobAndStatus(List<String> jobIds, Status status);

    long countByJobInAndCreatedAtBetween(List<String> jobIds, LocalDateTime last7Days, LocalDateTime now);

    long countByJobInAndStatusAndCreatedAtBetween(List<String >jobIds, Status status, LocalDateTime last7Days, LocalDateTime now);

    List<Application> findTop5ByJobInOrderByCreatedAtDesc(List<String> jobIds);

}
