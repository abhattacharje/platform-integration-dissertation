package com.compliance.documentvalidator.repository;

import com.compliance.documentvalidator.entity.UserCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends MongoRepository<UserCredentials, String> {

    Optional<UserCredentials> findById(String clientId);
}
