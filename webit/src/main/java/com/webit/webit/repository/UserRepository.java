package com.webit.webit.repository;

import com.webit.webit.dto.request.UserDTORequest;
import com.webit.webit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

    List<User> findAllByUserIdIn(List<String> userIds);

    List<User> findByUserIdIn(List<String> userIds);
}
