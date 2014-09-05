package io.pivotal.payup.web.view;

public class Account {
    private String username;
    private long balance;

    public Account(String username, long balance) {
        this.username = username;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public long getBalance() {
        return balance;
    }
}
