package io.pivotal.payup.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsControllerTest {
    private TransactionsController controller;

    @Mock
    private TransactionService service;

    @Before
    public void setUp() throws Exception {
      controller = new TransactionsController(service);
    }

    @Test
    public void shouldShowAllTransactionsForAnAccount(){
        ArrayList transactionsList = new ArrayList();
        transactionsList.add(new Transaction("Checking","Deposit","For Credit Card Bill",2000L,2000L));

        Mockito.when(service.getTransactions("Checking")).thenReturn(transactionsList);

        ModelAndView modelAndView= controller.listTransactions("Checking");

        ArrayList<TransactionView> transactionViews = (ArrayList<TransactionView>) modelAndView.getModel().get("transactions");
        TransactionView firstTransactionView = transactionViews.get(0);
        assertThat(firstTransactionView.getAccountName(), equalTo("Checking"));
        assertThat(firstTransactionView.getTransactionType(), equalTo("Deposit"));
        assertThat(firstTransactionView.getTransactionAmount(), equalTo(2000L));

        assertThat(modelAndView.getViewName(), is("transactions/index"));
    }
}
