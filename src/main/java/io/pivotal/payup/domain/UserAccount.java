package io.pivotal.payup.domain;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {

    @Id
    private String username;

    private String password;
    private long balance;

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = encryptPassword(password);
        this.balance = 0;
    }

//    Needed for Spring JPA
    @SuppressWarnings("UnusedDeclaration")
    protected UserAccount() {
    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getBalance() {
        return balance;
    }
}
