package io.pivotal.payup.web;

import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.view.Account;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountControllerTest {

    private AccountService service;
    private AccountController controller;
    private String name;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(AccountService.class);
        controller = new AccountController(service);
        name = "Savings";
    }

    @Test
    public void shouldRedirectToAccountDetailPageWhenAccountCreated() {
        ModelAndView modelAndView = controller.createAccount(name);

        assertThat(modelAndView.getViewName(), equalTo("redirect:/accounts/Savings"));
        Mockito.verify(service).createAccount(name);
    }

    @Test
    public void shouldShowAccount() {
        ModelAndView modelAndView = controller.showAccount(name);

        assertThat(modelAndView.getViewName(), equalTo("accounts/show"));
        Account account = (Account) modelAndView.getModel().get("account");
        assertThat(account.getName(), equalTo(name));
        assertThat(account.getBalance(), equalTo(0L));
    }

}
