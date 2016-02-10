package io.pivotal.payup.exceptions;

public class AmountExceedsAccountBalanceException extends Exception {

    public AmountExceedsAccountBalanceException(String message) {
        super(message);
    }
}
