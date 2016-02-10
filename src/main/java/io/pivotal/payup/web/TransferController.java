package io.pivotal.payup.web;

import io.pivotal.payup.services.AmountExceedsAccountBalanceException;
import io.pivotal.payup.services.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

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

        try {
            transferService.initiateTransfer(fromAccountName, toAccountName, amount, description);
        }
        catch (AmountExceedsAccountBalanceException exception){
            LOGGER.info(exception.getMessage(), exception);
            return new ModelAndView("/transfers/new","errorMessage", exception.getMessage());
        }

       return new ModelAndView("redirect:/accounts");
    }

}
