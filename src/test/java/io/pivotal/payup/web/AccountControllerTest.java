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
import static org.mockito.Mockito.*;

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

}
