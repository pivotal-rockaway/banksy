package io.pivotal.payup.web;

import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.view.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

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
        Account account = (Account) modelAndView.getModel().get("account");
        assertThat(account.getName(), equalTo(name));
        assertThat(account.getBalance(), equalTo(0L));
    }

    @Test
    public void shouldDepositAmount(){
        when(service.getBalance("Savings")).thenReturn(200L);

        ModelAndView modelAndView = controller.depositAmount(name, "200");
        assertThat(modelAndView.getViewName(), equalTo("redirect:/accounts/Savings"));
        verify(service).depositAmount(name, 200L);

        Account account = (Account) modelAndView.getModel().get("account");
        assertThat(account.getName(), equalTo(name));
        assertThat(account.getBalance(), equalTo(200L));
    }

    @Test
    public void shouldWithdrawAmount(){
        when(service.getBalance("Savings")).thenReturn(20L);

        ModelAndView modelAndView = controller.withdrawAmount(name, "200");
        assertThat(modelAndView.getViewName(), equalTo("redirect:/accounts/Savings"));
        verify(service).withdrawAmount(name, 200L);
        Account account = (Account) modelAndView.getModel().get("account");
        assertThat(account.getName(),equalTo(name));
        assertThat(account.getBalance(),equalTo(20L));
    }
}
