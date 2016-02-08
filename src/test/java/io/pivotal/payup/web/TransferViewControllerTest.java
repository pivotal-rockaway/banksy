package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.AmountExceedsAccountBalanceException;
import io.pivotal.payup.services.TransferService;
import io.pivotal.payup.web.view.AccountView;
import org.fluentlenium.adapter.FluentTest;
import org.hibernate.mapping.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TransferViewControllerTest {

    @Mock
    private TransferService service;
    @Mock
    private AccountService accountService;
    private TransferController controller;

    private final String fromAccountName = "Checking 1";
    private final String toAccountName = "Checking 2";
    private final String amount = "1000";
    private final String description = "Credit Card Bill";


    @Before
    public void setUp() throws Exception {
        controller = new TransferController(service, accountService);
    }


    @Test
    public void shouldShowNewTransferForm() {
        ModelAndView modelAndView = controller.newTransferForm();
        AccountView accountView = (AccountView) modelAndView.getModel().get("account");
        assertThat(modelAndView.getViewName(), equalTo("/transfers/new"));
    }

    @Test
    public void shouldRedirectToAccountsIndexPageWhenTransferInitiated() throws AmountExceedsAccountBalanceException {
        ModelAndView modelAndView = controller.createTransfer(fromAccountName, toAccountName, amount, description);
        verify(service).initiateTransfer(fromAccountName, toAccountName, amount, description);
    }

    @Test
    public void shouldCheckNegativeBalanceTransfer() throws AmountExceedsAccountBalanceException {
        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(new Account("Checking 1", 500L));
        accounts.add(new Account("Checking 2", 1000L));


        doThrow(new AmountExceedsAccountBalanceException("You Can't Exceed Your Current Balance")).when(service).initiateTransfer(fromAccountName, toAccountName, amount, description);
         ModelAndView modelAndView = controller.createTransfer(fromAccountName, toAccountName, amount, description);
        AccountView accountView = (AccountView) modelAndView.getModel().get("accounts");
        assertThat(accountView.getErrorMessage(), equalTo("You Can't Exceed Your Current Balance"));

    }
}
