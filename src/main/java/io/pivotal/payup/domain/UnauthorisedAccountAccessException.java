package io.pivotal.payup.domain;

public class UnauthorisedAccountAccessException extends Exception {
    public UnauthorisedAccountAccessException(String message) {
        super(message);
    }
}
