package io.pivotal.payup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    public String accountName;
    public String transType;
    public String transDesc;
    public Long   transAmount;
    public Long   availBalance;

    public Transaction() {
    }

    public Transaction(String accountName, String transType, String transDesc,
                       Long transAmount, Long availBalance) {
        this.accountName = accountName;
        this.transType = transType;
        this.transDesc = transDesc;
        this.transAmount = transAmount;
        this.availBalance = availBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(Long availBalance) {
        this.availBalance = availBalance;
    }
}
