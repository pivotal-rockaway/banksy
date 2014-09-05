package io.pivotal.payup.domain;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class UserAccountTest {

    @Test
    public void shouldEncryptPasswordsOnCreation() {
        String password = "somepassword";
        UserAccount account = new UserAccount("someusername", password);
        assertThat(account.getPassword(), not(equalTo(password)));
        assertTrue(BCrypt.checkpw(password, account.getPassword()));
    }

    @Test
    public void newAccountsShouldHaveZeroBalance() throws UnauthorisedAccountAccessException {
        UserAccount account = new UserAccount("someusername", "somepassword");
        assertThat(account.getBalance("somepassword"), equalTo(0L));
    }

    @Test(expected = UnauthorisedAccountAccessException.class)
    public void shouldThrowExceptionWhenFetchingBalanceWithWrongPassword() throws UnauthorisedAccountAccessException {
        UserAccount account = new UserAccount("someusername", "somepassword");
        account.getBalance("wrongpassword");
    }

}