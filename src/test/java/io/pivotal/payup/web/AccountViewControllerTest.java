package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.AmountExceedsAccountBalanceException;
import io.pivotal.payup.web.view.AccountView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountViewControllerTest {

    @Mock private AccountService service;
    private AccountController controller;
    private final String name = "Savings";

    @Before
    public void setUp() throws Exception {
        controller = new AccountController(service);
    }

    @Test
    public void shouldRedirectToAccountDetailPageWhenAccountCreated() {
        String viewName = controller.createAccount(name);

        assertThat(viewName, equalTo("redirect:/accounts/Savings"));
        verify(service).createAccount(name);
    }

    @Test
    public void shouldShowAccount() {
        ModelAndView modelAndView = controller.showAccount(name);

        assertThat(modelAndView.getViewName(), equalTo("accounts/show"));
        AccountView accountView = (AccountView) modelAndView.getModel().get("account");
        assertThat(accountView.getName(), equalTo(name));
        assertThat(accountView.getBalance(), equalTo(0L));
    }

    @Test
    public void shouldDepositAmount() {
        when(service.getBalance("Savings")).thenReturn(200L);

        ModelAndView modelAndView = controller.depositAmount(name, "200");
        assertThat(modelAndView.getViewName(), equalTo("redirect:/accounts/Savings"));
        verify(service).depositAmount(name, 200L);

        AccountView accountView = (AccountView) modelAndView.getModel().get("account");
        assertThat(accountView.getName(), equalTo(name));
        assertThat(accountView.getBalance(), equalTo(200L));
    }

    @Test
    public void shouldWithdrawAmount() throws AmountExceedsAccountBalanceException {
        when(service.getBalance("Savings")).thenReturn(20L);

        ModelAndView modelAndView = controller.withdrawAmount(name, "200");
        assertThat(modelAndView.getViewName(), equalTo("accounts/show"));
        verify(service).withdrawAmount(name, 200L);
        AccountView accountView = (AccountView) modelAndView.getModel().get("account");
        assertThat(accountView.getName(), equalTo(name));
        assertThat(accountView.getBalance(), equalTo(20L));
        assertNull(accountView.getErrorMessage());
    }


    @Test
    public void shouldShowErrorWhileWithdrawingMoreThanBalance() throws AmountExceedsAccountBalanceException{
        when(service.getBalance("Savings")).thenReturn(20L);
        doThrow(new AmountExceedsAccountBalanceException("You Can't Exceed Your Current Balance")).when(service).withdrawAmount(name, 1000);

        ModelAndView modelAndView = controller.withdrawAmount(name, "1000");
        assertThat(modelAndView.getViewName(), equalTo("accounts/show"));
        AccountView accountView = (AccountView) modelAndView.getModel().get("account");
        assertThat(accountView.getErrorMessage(), equalTo("You Can't Exceed Your Current Balance"));
        assertThat(accountView.getName(),equalTo(name));
        assertThat(accountView.getBalance(),equalTo(20L));
    }



    @Test
    public void shouldListAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(new Account("Checking 1"));
        accounts.add(new Account("Checking 2"));

        when(service.getAllAccounts()).thenReturn(accounts);
        ModelAndView modelAndView = controller.listAllAccounts();
        assertThat(modelAndView.getViewName(), equalTo("accounts/index"));
        ArrayList<AccountView> accountViews = (ArrayList<AccountView>) modelAndView.getModel().get("accounts");
        assertThat(accountViews.get(0).getName(), equalTo("Checking 1"));
        assertThat(accountViews.get(1).getName(), equalTo("Checking 2"));
    }

}
