package io.pivotal.payup.services;

import io.pivotal.payup.domain.UserAccount;
import io.pivotal.payup.persistence.UserAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
    public void shouldCreateUserAccountWithZeroBalance() {
        String username = "Alice";

        service.createUserAccount(username);

        ArgumentCaptor<UserAccount> createdUserAccount = ArgumentCaptor.forClass(UserAccount.class);
        verify(accountRepository).save(createdUserAccount.capture());
        assertThat(createdUserAccount.getValue().getUsername(), equalTo(username));
        assertThat(createdUserAccount.getValue().getBalance(), equalTo(0L));
    }

    @Test
    public void shouldRetrieveBalanceOfUserAccount() {
        String username = "someuser@mail.com";
        when(accountRepository.findOne(username))
                .thenReturn(new UserAccount(username));

        long balance = service.getBalance(username);
        assertThat(balance, equalTo(0L));

        verify(accountRepository).findOne(username);
    }
}
