package io.pivotal.payup.services;

import io.pivotal.payup.domain.UnauthorisedAccountAccessException;
import io.pivotal.payup.domain.UserAccount;
import io.pivotal.payup.persistence.UserAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private UserAccountRepository accountRepository;
    private AccountService service;

    @Before
    public void setUp() {
        accountRepository = Mockito.mock(UserAccountRepository.class);
        service = new AccountService(accountRepository);
    }

    @Test
    public void shouldCreateUserAccountWithZeroBalance() throws UnauthorisedAccountAccessException {
        String username = "Alice";
        String password = "gr8P$!sswΩrd";

        service.createUserAccount(username, password);

        ArgumentCaptor<UserAccount> createdUserAccount = ArgumentCaptor.forClass(UserAccount.class);
        verify(accountRepository).save(createdUserAccount.capture());
        assertThat(createdUserAccount.getValue().getUsername(), equalTo(username));
        assertThat(createdUserAccount.getValue().getPassword(), not(equalTo(password)));
        assertThat(createdUserAccount.getValue().getBalance(password), equalTo(0L));
    }

    @Test
    public void shouldRetrieveBalanceOfUserAccount() throws UnauthorisedAccountAccessException {
        String username = "someuser@mail.com";
        String password = "strongpassword";
        when(accountRepository.findOne(username))
                .thenReturn(new UserAccount(username, password));

        long balance = service.getBalance(username, password);
        assertThat(balance, equalTo(0L));

        verify(accountRepository).findOne(username);
    }

    @Test(expected = UnauthorisedAccountAccessException.class)
    public void shouldThrowExceptionWhenUsernameDoesNotExist() throws UnauthorisedAccountAccessException {
        when(accountRepository.findOne(any()))
                .thenReturn(null);

        service.getBalance("some user", "some password");
    }

    @Test(expected = UnauthorisedAccountAccessException.class)
    public void shouldThrowExceptionWhenPasswordIsWrong() throws UnauthorisedAccountAccessException {
        String username = "some user";
        String password = "some password";
        when(accountRepository.findOne(username))
                .thenReturn(new UserAccount(username, password));

        service.getBalance(username, "wrong password");
    }
}