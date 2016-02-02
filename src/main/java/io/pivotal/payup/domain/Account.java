package io.pivotal.payup.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {

    @Id
    private String name;

    private long balance;

    public Account(String name) {
        this.name = name;
        this.balance = 0;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    /** Needed for Spring JPA */
    protected Account() {
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

}
