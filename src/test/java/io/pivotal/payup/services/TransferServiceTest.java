package io.pivotal.payup.services;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.persistence.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

    @Mock private AccountRepository accountRepository;
    private TransferService transferService;

    private final String fromAccountName = "Checking 1";
    private final String toAccountName = "Checking 2";
    private final String description = "Credit Card Bill";
    private final String amount = "250";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        transferService = new TransferService(accountRepository);
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
}