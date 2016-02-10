package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.AmountExceedsAccountBalanceException;
import io.pivotal.payup.web.view.AccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "new", method = GET)
    public String newAccountForm() {
        return "accounts/new";
    }

    @RequestMapping(method = POST)
    public String createAccount(@RequestParam String name) {
        accountService.createAccount(name);
        return "redirect:/accounts/" + name;
    }

    @RequestMapping(method = GET, value = "/{name}")
    public ModelAndView showAccount(@PathVariable String name) {
        long balance = accountService.getBalance(name);
        return new ModelAndView("accounts/show", "account", new AccountView(name, balance, ""));
    }

    @RequestMapping(method = POST, value = "/{name}/deposit")
    public ModelAndView depositAmount(@PathVariable String name, @RequestParam String amount){
        accountService.depositAmount(name, Long.parseLong(amount));
        long balance = accountService.getBalance(name);
        return new ModelAndView("redirect:/accounts/" + name, "account", new AccountView(name, balance, ""));
    }

    @RequestMapping(method = POST, value = "/{name}/withdraw")
    public  ModelAndView withdrawAmount(@PathVariable String name, @RequestParam String amount) throws AmountExceedsAccountBalanceException{
        AccountView accountView;
        try {
            accountService.withdrawAmount(name, Long.parseLong(amount));
            accountView = new AccountView(name, accountService.getBalance(name));
        } catch (AmountExceedsAccountBalanceException exception) {
            logger.error(exception.getMessage(), exception);
            accountView = new AccountView(name, accountService.getBalance(name), exception.getMessage());
        }
        return new ModelAndView("accounts/show" , "account", accountView);
    }

    @RequestMapping(method = GET, value = "" )
    public ModelAndView listAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountView> accountViews = new ArrayList<>();
        for(Account account : accounts) {
            accountViews.add(new AccountView(account.getName(), account.getBalance(), ""));
        }
        return  new ModelAndView("accounts/index", "accounts", accountViews);
    }

}
