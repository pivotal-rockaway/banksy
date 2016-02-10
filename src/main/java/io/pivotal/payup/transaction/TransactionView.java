package io.pivotal.payup.transaction;

public class TransactionView {
    private String accountName;
    private String transactionType;
    private String transactionDescription;
    private Long transactionAmount;
    private Long availableBalance;

    public TransactionView(String accountName, String transactionType, String transactionDescription,
                           Long transactionAmount, Long availableBalance) {
        this.accountName = accountName;
        this.transactionType = transactionType;
        this.transactionDescription = transactionDescription;
        this.transactionAmount = transactionAmount;
        this.availableBalance = availableBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transType) {
        this.transactionType = transType;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Long getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }
}
