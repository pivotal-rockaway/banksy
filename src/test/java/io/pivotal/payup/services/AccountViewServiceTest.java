package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.internal.verification.VerificationModeFactory.times;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountViewServiceTest {

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

    @Test
    public void shouldAddBalanceToExistingAccount(){
        Account account = new Account("Salary");
        when(accountRepository.findOne("Salary")).thenReturn(account);

        service.depositAmount("Salary", 50L);
        assertThat(service.getBalance("Salary"), equalTo(50L));

        service.depositAmount("Salary", 25L);
        assertThat(service.getBalance("Salary"), equalTo(75L));
        verify(accountRepository, times(2)).save(account);
    }

    @Test
    public void shouldReturnExistingAccount(){
        Account account = new Account("Salary");
        when(accountRepository.findOne("Salary")).thenReturn(account);
        assertThat(service.getAccount("Salary"), equalTo(account));
    }

    @Test
    public void shouldWithdrawAmountFromAccount(){
        Account account = new Account("Salary");
        when(accountRepository.findOne("Salary")).thenReturn(account);

        service.depositAmount("Salary", 2000L);
        service.withdrawAmount("Salary", 100L);

        assertThat(service.getBalance("Salary"), equalTo(1900L));
        verify(accountRepository, times(2)).save(account);
    }

    @Test
    public void shouldListAllAccounts(){
        Account accountOne = new Account("Home");
        Account accountTwo = new Account("Salary");
        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(accountOne);
        accounts.add(accountTwo);

        when(accountRepository.findAll()).thenReturn(accounts);

        assertThat(service.getAllAccounts(), equalTo(accounts));
    }
}
