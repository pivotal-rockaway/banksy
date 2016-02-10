package io.pivotal.payup.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Before
    public void setup() {
       transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void showAllTransactions_returnsTransactionsForAnAccount() throws Exception {
        ArrayList<Transaction> persistedTransactions = new ArrayList<>();
        persistedTransactions.add(new Transaction("foo", "bar", "baz", 42L, 84L));
        persistedTransactions.add(new Transaction("foo", "bar", "baz", 42L, 84L));
        when(transactionRepository.findByAccountName("Checking")).thenReturn(persistedTransactions);

        List<Transaction> transactions = transactionService.getTransactions("Checking");

        assertThat(transactions.size(), is(2));
    }

    @Test
    public void shouldCreateTransaction() throws Exception {
        transactionService.createTransaction("Checking","Deposit","",50L,50L);

        ArgumentCaptor<Transaction> createdTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(createdTransaction.capture());

        Transaction persistedTransaction = createdTransaction.getValue();
        assertThat(persistedTransaction.getAccountName(), is("Checking"));
    }
}