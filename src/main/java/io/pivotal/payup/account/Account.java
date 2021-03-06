package io.pivotal.payup.account;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    private String name;

    private long balance;

    protected Account() {
    }

    public Account(String name) {
        this.name = name;
        this.balance = 0;
    }

    public Account(String name,long balance) {
        this.name = name;
        this.balance = balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

}
