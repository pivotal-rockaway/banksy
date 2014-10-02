package io.pivotal.payup.services;

import io.pivotal.payup.domain.UserAccount;
import io.pivotal.payup.persistence.UserAccountRepository;

public class AccountService {

    private final UserAccountRepository accountRepository;

    public AccountService(UserAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createUserAccount(String username) {
        UserAccount account = new UserAccount(username);
        accountRepository.save(account);
    }

    public long getBalance(String username) {
        UserAccount userAccount = accountRepository.findOne(username);
        return userAccount.getBalance();
    }

}
