package io.pivotal.payup.json;

//For Gson
public class GetBalanceResponse {
    private long balance;

    public GetBalanceResponse(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }
}
