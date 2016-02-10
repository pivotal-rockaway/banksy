package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class AccountServiceTest {
    private AccountRepository accountRepository;
    private AccountService accountService;
    private TransactionService transactionService;
    private Account salaryAccount;
    private Account homeAccount;

    @Before
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transactionService = Mockito.mock(TransactionService.class);
        accountService = new AccountService(accountRepository, transactionService);

        salaryAccount = new Account("Salary");
        homeAccount = new Account("Home");
    }

    @Test
    public void shouldCreateAccountWithZeroBalance() {
        accountService.createAccount("Savings");

        ArgumentCaptor<Account> createdAccount = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(createdAccount.capture());
        assertThat(createdAccount.getValue().getName(), equalTo("Savings"));
        assertThat(createdAccount.getValue().getBalance(), equalTo(0L));
    }

    @Test
    public void shouldRetrieveBalanceOfAccount() {
        when(accountRepository.findOne("Rent and bills")).thenReturn(new Account("Rent and bills"));

        long balance = accountService.getBalance("Rent and bills");
        assertThat(balance, equalTo(0L));

        verify(accountRepository).findOne("Rent and bills");
    }

    @Test
    public void shouldAddBalanceToExistingAccount(){
        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);

        accountService.depositAmount("Salary", 50L);
        assertThat(accountService.getBalance("Salary"), equalTo(50L));

        accountService.depositAmount("Salary", 25L);
        assertThat(accountService.getBalance("Salary"), equalTo(75L));
        verify(accountRepository, times(2)).save(salaryAccount);
    }

    @Test
    public void shouldCreateTransactionForDeposit() {
        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);

        accountService.depositAmount("Salary", 50L);

        verify(transactionService).createTransaction("Salary", "Deposit", "", 50L, 50L);
    }

    @Test
    public void shouldReturnExistingAccount() {
        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);
        assertThat(accountService.getAccount("Salary"), equalTo(salaryAccount));
    }

    @Test
    public void shouldWithdrawAmountFromAccount() throws AmountExceedsAccountBalanceException{
        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);

        accountService.depositAmount("Salary", 2000L);
        accountService.withdrawAmount("Salary", 100L);

        assertThat(accountService.getBalance("Salary"), equalTo(1900L));
        verify(accountRepository, times(2)).save(salaryAccount);
    }

    @Test
    public void shouldCreateTransactionForWithdraw() throws AmountExceedsAccountBalanceException {
        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);
        accountService.depositAmount("Salary", 100L);
        accountService.withdrawAmount("Salary",50L);
        verify(transactionService).createTransaction("Salary", "Withdraw", "", 50L,50L);
    }

    @Test
    public void shouldListAllAccounts(){
        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(homeAccount);
        accounts.add(salaryAccount);

        when(accountRepository.findAll()).thenReturn(accounts);

        assertThat(accountService.getAllAccounts(), equalTo(accounts));
    }

    @Test(expected=AmountExceedsAccountBalanceException.class)
    public void shouldRaiseAmountExceedsAccountBalanceException() throws AmountExceedsAccountBalanceException {
        salaryAccount.setBalance(20);

        when(accountRepository.findOne("Salary")).thenReturn(salaryAccount);
        try {
            accountService.withdrawAmount("Salary", 350);
        } catch (AmountExceedsAccountBalanceException exception) {
            assertThat(exception.getMessage(), equalTo("You Can't Exceed Your Current Balance"));
            throw exception;
        }
        fail("No amount exceeds balance exception thrown");
    }
}
