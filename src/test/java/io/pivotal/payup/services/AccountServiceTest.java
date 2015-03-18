package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService service;

    @Before
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        service = new AccountService(accountRepository);
    }

    @Test
    public void shouldCreateAccountWithZeroBalance() {
        String name = "Savings";

        service.createAccount(name);

        ArgumentCaptor<Account> createdAccount = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(createdAccount.capture());
        assertThat(createdAccount.getValue().getName(), equalTo(name));
        assertThat(createdAccount.getValue().getBalance(), equalTo(0L));
    }

    @Test
    public void shouldRetrieveBalanceOfAccount() {
        String name = "Rent and bills";
        when(accountRepository.findOne(name)).thenReturn(new Account(name));

        long balance = service.getBalance(name);
        assertThat(balance, equalTo(0L));

        verify(accountRepository).findOne(name);
    }

}
