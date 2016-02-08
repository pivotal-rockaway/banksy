package io.pivotal.payup.services;

/**
 * Created by pivotal on 2/8/16.
 */
public class AmountExceedsAccountBalanceException extends Exception {

    public AmountExceedsAccountBalanceException(String message) {
        super(message);
    }
}
