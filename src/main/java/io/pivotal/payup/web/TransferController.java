package io.pivotal.payup.web;

import io.pivotal.payup.domain.Account;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.AmountExceedsAccountBalanceException;
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

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @RequestMapping(value = "new", method = GET)
    public ModelAndView newTransferForm() {
        return new ModelAndView("/transfers/new");
    }

    @RequestMapping(value = "create", method = POST)
    public ModelAndView createTransfer(@RequestParam String fromAccountName,
                                 @RequestParam String toAccountName,
                                 @RequestParam String amount, @RequestParam String description) throws AmountExceedsAccountBalanceException {
        ArrayList<AccountView> accountViews =null;
        try {
            transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);
        }
        catch (AmountExceedsAccountBalanceException exception){
            return new ModelAndView("/transfers/new","errorMessage", exception.getMessage());
        }

       return new ModelAndView("redirect:/accounts");
    }
}
