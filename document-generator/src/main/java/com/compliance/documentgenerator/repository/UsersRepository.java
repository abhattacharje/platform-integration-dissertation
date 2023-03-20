package com.compliance.documentgenerator.repository;

import com.compliance.documentgenerator.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {

    Optional<Users> findById(String userName);
}
