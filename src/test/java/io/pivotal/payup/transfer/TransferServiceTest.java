package io.pivotal.payup.transfer;

import io.pivotal.payup.account.Account;
import io.pivotal.payup.account.AccountRepository;
import io.pivotal.payup.exceptions.AmountExceedsAccountBalanceException;
import io.pivotal.payup.transaction.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

    @Mock private AccountRepository accountRepository;
    private TransferService transferService;
    private TransactionService transactionService;

    private final String fromAccountName = "Checking 1";
    private final String toAccountName = "Checking 2";
    private final String description = "Credit Card Bill";
    private final String amount = "250";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transactionService = Mockito.mock(TransactionService.class);
        transferService = new TransferService(accountRepository, transactionService);
    }

    @Test
    public void testInitiateTransfer() throws Exception {
        Account fromAccount = mock(Account.class);
        when(accountRepository.findOne(fromAccountName)).thenReturn(fromAccount);

        Account toAccount = mock(Account.class);
        when(accountRepository.findOne(toAccountName)).thenReturn(toAccount);

        when(fromAccount.getBalance()).thenReturn(1000L);
        when(toAccount.getBalance()).thenReturn(0L);

        transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);

        verify(fromAccount).setBalance(750L);
        verify(toAccount).setBalance(250L);

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    public void shouldCreateTransactionForTransfer() throws  Exception{
        Account fromAccount = mock(Account.class);
        when(accountRepository.findOne(fromAccountName)).thenReturn(fromAccount);

        Account toAccount = mock(Account.class);
        when(accountRepository.findOne(toAccountName)).thenReturn(toAccount);

        when(fromAccount.getBalance()).thenReturn(1000L);
        when(toAccount.getBalance()).thenReturn(0L);

        transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);
        verify(transactionService).createTransaction(fromAccountName,
                "Withdraw",description,Long.parseLong(amount),750L);
        verify(transactionService).createTransaction(toAccountName,
                "Deposit",description,Long.parseLong(amount),250L);

    }

    @Test(expected=AmountExceedsAccountBalanceException.class)
    public void shouldRaiseExceedAccountBalanceException() throws AmountExceedsAccountBalanceException{

        Account fromAccount = mock(Account.class);
        when(accountRepository.findOne(fromAccountName)).thenReturn(fromAccount);

        Account toAccount = mock(Account.class);
        when(accountRepository.findOne(toAccountName)).thenReturn(toAccount);

        when(fromAccount.getBalance()).thenReturn(0L);
        when(toAccount.getBalance()).thenReturn(50L);

        try{
            transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);
        }
        catch(AmountExceedsAccountBalanceException exception){
           Assert.assertThat(exception.getMessage(), equalTo("You Can't Exceed Your Current Balance"));
           throw exception;
        }


    }


}