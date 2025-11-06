package com.webit.webit.repository;


import com.webit.webit.model.Job;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Page<Job> findAllByCompany_UserId(String userId, Pageable pageable);

    List<Job> findAllByCompany_UserId(String userId);

    Optional<Job> findByJobId(String jobId);

    List<Job> findByJobIdIn(List<String> jobIds);

    long countByCompany_UserIdAndIsClosedFalse(String userId);

    long countByCompany_UserIdAndCreatedAtBetween(String userId, LocalDateTime last7Days, LocalDateTime now);

    List<Job> findTop5ByCompany_UserIdOrderByCreatedAtDesc(String userId);
}
