package io.pivotal.payup.web.view;

public class AccountView {

    private String name;
    private long balance;

    public AccountView(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

}
