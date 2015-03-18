package io.pivotal.payup.persistence;

import io.pivotal.payup.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}
