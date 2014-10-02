package io.pivotal.payup.web;

import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.view.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/accounts")
public class UserAccountController {

    private final AccountService accountService;

    @Autowired
    public UserAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "new", method = GET)
    public String newUserAccountForm() {
        return "accounts/new";
    }

    @RequestMapping(method = POST)
    public ModelAndView createUser(@RequestParam String username) {
        accountService.createUserAccount(username);
        return new ModelAndView("redirect:/accounts/" + username);
    }

    @RequestMapping(method = GET, value = "{username}")
    public ModelAndView showUser(@PathVariable String username) {
        long balance = accountService.getBalance(username);
        return new ModelAndView("accounts/show", "account", new Account(username, balance));
    }

}
