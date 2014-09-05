package io.pivotal.payup.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserAccountTest {

    @Test
    public void newAccountsShouldHaveZeroBalance() {
        UserAccount account = new UserAccount("someusername");
        assertThat(account.getBalance(), equalTo(0L));
    }

}
