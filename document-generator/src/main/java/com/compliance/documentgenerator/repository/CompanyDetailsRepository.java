package com.compliance.documentgenerator.repository;

import com.compliance.documentgenerator.entity.CompanyDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDetailsRepository extends MongoRepository<CompanyDetails, String> {

    Optional<CompanyDetails> findById(String userName);

    Optional<CompanyDetails> findByCompanyId(String companyId);
}
