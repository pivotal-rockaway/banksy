package io.pivotal.payup.transaction;

import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    public List<Transaction> getTransactions(String accountName){
        return transactionRepository.findByAccountName(accountName);
    }

    public void createTransaction(String accounName, String transType, String transDesc, long transAmount, long availBalance) {
        Transaction transaction =new Transaction(accounName,transType,transDesc,transAmount,availBalance);
        transactionRepository.save(transaction);

    }
}
