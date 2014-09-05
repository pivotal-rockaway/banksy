package io.pivotal.payup.persistence;

import io.pivotal.payup.domain.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
}
