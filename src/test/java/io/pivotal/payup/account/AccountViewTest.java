package io.pivotal.payup.account;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountViewTest {

    @Test
    public void newAccountsShouldHaveZeroBalance() {
        Account account = new Account("someaccountname");
        assertThat(account.getBalance(), equalTo(0L));
    }

    @Test
    public void shouldUpdateBalance(){
        Account account = new Account("Checking AccountView");
        account.setBalance(65L);
        assertThat(account.getBalance(), equalTo(65L));
    }

}
