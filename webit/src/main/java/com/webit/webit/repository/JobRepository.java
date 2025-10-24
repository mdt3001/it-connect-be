package com.webit.webit.repository;


import com.webit.webit.model.Job;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Page<Job> findAllByCompany_UserId(String userId, Pageable pageable);
    Optional<Job> findByJobId(String jobId);
}
