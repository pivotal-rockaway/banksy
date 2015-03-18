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
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "new", method = GET)
    public String newAccountForm() {
        return "accounts/new";
    }

    @RequestMapping(method = POST)
    public ModelAndView createAccount(@RequestParam String name) {
        accountService.createAccount(name);
        return new ModelAndView("redirect:/accounts/" + name);
    }

    @RequestMapping(method = GET, value = "{name}")
    public ModelAndView showAccount(@PathVariable String name) {
        long balance = accountService.getBalance(name);
        return new ModelAndView("accounts/show", "account", new Account(name, balance));
    }

}
