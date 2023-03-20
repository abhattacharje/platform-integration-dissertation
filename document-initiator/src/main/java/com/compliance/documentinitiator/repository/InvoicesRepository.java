package com.compliance.documentinitiator.repository;

import com.compliance.documentinitiator.entity.Invoices;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoicesRepository extends MongoRepository<Invoices, String> {

    Optional<Invoices> findById(String invoiceId);

    @Override
    List<Invoices> findAll();
}
