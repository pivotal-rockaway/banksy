package io.pivotal.payup.web;

import io.pivotal.payup.domain.Transaction;
import io.pivotal.payup.services.TransactionService;
import io.pivotal.payup.web.view.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@RequestMapping("/")
public class TransactionsController {

    private final TransactionService service;

    @Autowired
    public TransactionsController(TransactionService service) {
        this.service = service;
    }

    @RequestMapping(method = GET ,value = "/{accountName}/transactions")
    public ModelAndView listTransactions(@PathVariable String accountName) {
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) service.getTransactions(accountName);

        ArrayList<TransactionView> transactionViews = new ArrayList();
        for (Transaction transaction : transactions) {
            transactionViews.add(new TransactionView(transaction.getAccountName(),
                    transaction.getTransType(),transaction.getTransDesc(),transaction.getTransAmount(),transaction.getAvailBalance()));

        }

        return new ModelAndView("transactions/index", "transactions", transactionViews);
    }

}
