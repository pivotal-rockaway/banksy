package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.TransferService;
import io.pivotal.payup.web.view.AccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by pivotal on 2/3/16.
 */

@Controller
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    private final AccountService accountService;
    private static String message = "";

    @Autowired
    public TransferController(TransferService transferService,AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    @RequestMapping(value = "new", method = GET)
    public ModelAndView newTransferForm() {
        String name = "";
        Long balance = 0L;
        return new ModelAndView("/transfers/new", "accounts", new AccountView(name, balance, message));

    }



    @RequestMapping(value = "create", method = POST)
    public ModelAndView createTransfer(@RequestParam String fromAccountName,
                                 @RequestParam String toAccountName,
                                 @RequestParam String amount, @RequestParam String description) {
        message = transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);
        ArrayList<Account> accounts = (ArrayList<Account>) accountService.getAllAccounts();
        ArrayList<AccountView> accountViews = new ArrayList<AccountView>();
        for(Account account : accounts){
            accountViews.add(new AccountView(account.getName(), account.getBalance(),message));
        }
        if(message !=null && !message.equalsIgnoreCase(""))
            return new ModelAndView("redirect:/transfers/new","accounts",accountViews);
        else
            return new ModelAndView("redirect:/accounts","accounts",accountViews);
    }
}
