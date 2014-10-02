package io.pivotal.payup.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {

    @Id
    private String username;

    private long balance;

    public UserAccount(String username) {
        this.username = username;
        this.balance = 0;
    }

    /** Needed for Spring JPA */
    protected UserAccount() {
    }

    public String getUsername() {
        return username;
    }

    public long getBalance() {
        return balance;
    }

}
