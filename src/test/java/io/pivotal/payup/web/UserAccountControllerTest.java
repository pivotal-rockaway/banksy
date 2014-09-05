package io.pivotal.payup.web;

import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.view.Account;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserAccountControllerTest {


    private AccountService service;
    private UserAccountController controller;
    private String username;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(AccountService.class);
        controller = new UserAccountController(service);
        username = "big_dave";
    }

    @Test
    public void shouldRedirectToAccountDetailPageWhenUserCreated() {
        ModelAndView modelAndView = controller.createUser(username);

        assertThat(modelAndView.getViewName(), equalTo("redirect:/accounts/big_dave"));
        Mockito.verify(service).createUserAccount(username);
    }

    @Test
    public void shouldShowUser() {
        ModelAndView modelAndView = controller.showUser(username);

        assertThat(modelAndView.getViewName(), equalTo("accounts/show"));
        Account account = (Account) modelAndView.getModel().get("account");
        assertThat(account.getUsername(), equalTo(username));
        assertThat(account.getBalance(), equalTo(0L));
    }
}