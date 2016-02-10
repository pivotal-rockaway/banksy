package io.pivotal.payup.account;

public class AccountView {

    private String name;
    private long balance;



    private String errorMessage;

    public AccountView(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public AccountView(String name, long balance, String errorMessage) {
        this.name = name;
        this.balance = balance;
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
