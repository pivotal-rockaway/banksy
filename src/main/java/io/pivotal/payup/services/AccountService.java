package io.pivotal.payup.services;

import io.pivotal.payup.domain.UnauthorisedAccountAccessException;
import io.pivotal.payup.domain.UserAccount;
import io.pivotal.payup.persistence.UserAccountRepository;

public class AccountService {

    private final UserAccountRepository accountRepository;

    public AccountService(UserAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createUserAccount(String username, String password) {
        UserAccount account = new UserAccount(username, password);
        accountRepository.save(account);
    }

    public long getBalance(String username, String password) throws UnauthorisedAccountAccessException {
        UserAccount userAccount = accountRepository.findOne(username);
        if (userAccount != null) {
            return userAccount.getBalance();
        }
        else {
            throw new UnauthorisedAccountAccessException("User " + username + " does not exist");
        }
    }
}
