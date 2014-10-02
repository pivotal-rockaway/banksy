package io.pivotal.payup.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountTest {

    @Test
    public void newAccountsShouldHaveZeroBalance() {
        Account account = new Account("someaccountname");
        assertThat(account.getBalance(), equalTo(0L));
    }

}
