package io.pivotal.payup.transfer;

import io.pivotal.payup.Application;
import io.pivotal.payup.account.Account;
import io.pivotal.payup.account.AccountRepository;
import io.pivotal.payup.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TransferServiceIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    private TransferService transferService;
    @Autowired
    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        accountRepository.deleteAll();

        transferService = new TransferService(accountRepository, transactionService);
    }

    @Test
    public void testInitiateTransfer() throws Exception {
        Account fromAccount = new Account("fromAccountName");
        fromAccount.setBalance(1000L);
        Account toAccount = new Account("toAccountName");
        toAccount.setBalance(0L);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transferService.initiateTransfer("fromAccountName", "toAccountName", "250", "test transfer");

        fromAccount = accountRepository.findOne("fromAccountName");
        toAccount = accountRepository.findOne("toAccountName");

        assertThat(fromAccount.getBalance(), is(750L));
        assertThat(toAccount.getBalance(), is(250L));
    }
}
