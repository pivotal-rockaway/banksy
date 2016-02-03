package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.view.AccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

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
    public String createAccount(@RequestParam String name) {
        accountService.createAccount(name);
        return "redirect:/accounts/" + name;
    }

    @RequestMapping(method = GET, value = "/{name}")
    public ModelAndView showAccount(@PathVariable String name) {
        long balance = accountService.getBalance(name);
        return new ModelAndView("accounts/show", "account", new AccountView(name, balance));
    }

    @RequestMapping(method = POST, value = "/{name}/deposit")
    public ModelAndView depositAmount(@PathVariable String name, @RequestParam String amount){
        accountService.depositAmount(name, Long.parseLong(amount));
        long balance = accountService.getBalance(name);
        return new ModelAndView("redirect:/accounts/" + name, "account", new AccountView(name, balance));
    }

    @RequestMapping(method = POST, value = "/{name}/withdraw")
    public  ModelAndView withdrawAmount(@PathVariable String name, @RequestParam String amount){
        accountService.withdrawAmount(name, Long.parseLong(amount));
        long balance = accountService.getBalance(name);
        return new ModelAndView("redirect:/accounts/" + name, "account", new AccountView(name, balance));
    }

    @RequestMapping(method = GET, value = "" )
    public ModelAndView listAllAccounts() {
        ArrayList<Account> accounts = (ArrayList<Account>) accountService.getAllAccounts();
        ArrayList<AccountView> accountViews = new ArrayList<AccountView>();
        for(Account account : accounts){
            accountViews.add(new AccountView(account.getName(), account.getBalance()));
        }
        return  new ModelAndView("accounts/index", "accounts", accountViews);
    }
}
