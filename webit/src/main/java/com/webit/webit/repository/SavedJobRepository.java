package com.webit.webit.repository;

import com.webit.webit.model.Job;
import com.webit.webit.model.SavedJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SavedJobRepository extends MongoRepository<SavedJob, String> {
    List<SavedJob>  findAllByJobseeker(String userId);


}
