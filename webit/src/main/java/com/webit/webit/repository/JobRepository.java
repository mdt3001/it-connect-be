package com.webit.webit.repository;


import com.webit.webit.model.Job;
import com.webit.webit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {

}
