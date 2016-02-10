package io.pivotal.payup.persistence;

import io.pivotal.payup.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByAccountName(String accountName);
}