package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;

public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public TransferService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public void initiateTransfer(String fromAccountName, String toAccountName, String amount, String description) throws AmountExceedsAccountBalanceException {
        Account fromAccount = accountRepository.findOne(fromAccountName);
        Account toAccount = accountRepository.findOne(toAccountName);
        long longAmount = Long.parseLong(amount);
        if(fromAccount.getBalance() < longAmount )
            throw new AmountExceedsAccountBalanceException("You Can't Exceed Your Current Balance");
        else if(longAmount < 0)
            throw new AmountExceedsAccountBalanceException("You Can't Enter a Negative Amount");
        else {
            Long newFromAccountBalance = fromAccount.getBalance() - longAmount;
            Long newToAccountBalance = toAccount.getBalance() + longAmount;
            fromAccount.setBalance(newFromAccountBalance);
            toAccount.setBalance(newToAccountBalance);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transactionService.createTransaction(fromAccountName,"Withdraw",description,longAmount,newFromAccountBalance);
            transactionService.createTransaction(toAccountName,"Deposit",description,longAmount,newToAccountBalance);
        }
    }
}
