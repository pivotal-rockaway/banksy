package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;

import java.util.ArrayList;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public void createAccount(String name) {
        Account account = new Account(name);
        accountRepository.save(account);
    }

    public long getBalance(String name) {
        Account account = accountRepository.findOne(name);
        return account.getBalance();
    }

    public void depositAmount(String name, long amount) {
        Account account = accountRepository.findOne(name);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        transactionService.createTransaction(name,"Deposit","",amount,account.getBalance());
    }

    public Account getAccount(String name) {
        return accountRepository.findOne(name);
    }

    public ArrayList<Account> getAllAccounts() {
        return (ArrayList<Account>) accountRepository.findAll();
    }

    public void withdrawAmount(String name, long amount) throws AmountExceedsAccountBalanceException {
        Account account = accountRepository.findOne(name);
        Long newBalance = account.getBalance() - amount;
        if (newBalance < 0) {
            throw new AmountExceedsAccountBalanceException("You Can't Exceed Your Current Balance");
        } else {
            account.setBalance(newBalance);
            accountRepository.save(account);
            transactionService.createTransaction(name,"Withdraw", "", amount, account.getBalance());
        }
    }
}
