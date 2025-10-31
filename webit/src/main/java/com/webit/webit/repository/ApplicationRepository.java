package com.webit.webit.repository;

import com.webit.webit.dto.response.application.ApplicationResponseById;
import com.webit.webit.dto.response.application.MyApplicationResponse;
import com.webit.webit.model.Application;
import com.webit.webit.model.Job;
import com.webit.webit.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAllByApplicant(String userId);

    Page<Application> findAllByJob(String jobId, Pageable pageable);
}
