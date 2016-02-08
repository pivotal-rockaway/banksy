package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;

/**
 * Created by pivotal on 2/3/16.
 */
public class TransferService {

    private final AccountRepository accountRepository;
    private String errorMessage = "";

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String initiateTransfer(String fromAccountName, String toAccountName, String amount, String description) {
        Account fromAccount = accountRepository.findOne(fromAccountName);
        Account toAcount = accountRepository.findOne(toAccountName);
        long longAmount = Long.parseLong(amount);
        if(fromAccount.getBalance() < longAmount )
            errorMessage = "You can't Exceed Your Limit!";
        else if(longAmount < 0)
            errorMessage = "Amount can't be negative.";
        else {
            fromAccount.setBalance(fromAccount.getBalance() - longAmount);
            toAcount.setBalance(toAcount.getBalance() + longAmount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAcount);
        }
        return errorMessage;
    }
}
