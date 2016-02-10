package io.pivotal.payup.services;

public class AmountExceedsAccountBalanceException extends Exception {

    public AmountExceedsAccountBalanceException(String message) {
        super(message);
    }
}
