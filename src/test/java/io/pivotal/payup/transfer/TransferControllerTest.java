package io.pivotal.payup.transfer;

import io.pivotal.payup.account.Account;
import io.pivotal.payup.exceptions.AmountExceedsAccountBalanceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransferControllerTest {

    @Mock
    private TransferService service;
    private TransferController controller;

    private final String fromAccountName = "Checking 1";
    private final String toAccountName = "Checking 2";
    private final String amount = "1000";
    private final String description = "Credit Card Bill";

    @Before
    public void setUp() throws Exception {
        controller = new TransferController(service);
    }

    @Test
    public void shouldShowNewTransferForm() {
        ModelAndView modelAndView = controller.newTransferForm();
        assertThat(modelAndView.getViewName(), equalTo("/transfers/new"));
    }

    @Test
    public void shouldRedirectToAccountsIndexPageWhenTransferInitiated() throws AmountExceedsAccountBalanceException {
        controller.createTransfer(fromAccountName, toAccountName, amount, description);

        verify(service).initiateTransfer(fromAccountName, toAccountName, amount, description);
    }

    @Test
    public void shouldCheckNegativeBalanceTransfer() throws AmountExceedsAccountBalanceException {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("Checking 1", 500L));
        accounts.add(new Account("Checking 2", 1000L));

        doThrow(new AmountExceedsAccountBalanceException("You Can't Exceed Your Current Balance")).when(service).initiateTransfer(fromAccountName, toAccountName, amount, description);
         ModelAndView modelAndView = controller.createTransfer(fromAccountName, toAccountName, amount, description);
        String errorMessage = (String) modelAndView.getModel().get("errorMessage");

        assertThat(errorMessage, equalTo("You Can't Exceed Your Current Balance"));
    }
}
