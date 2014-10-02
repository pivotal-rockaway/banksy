package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(String name) {
        Account account = new Account(name);
        accountRepository.save(account);
    }

    public long getBalance(String name) {
        Account account = accountRepository.findOne(name);
        return account.getBalance();
    }

}
