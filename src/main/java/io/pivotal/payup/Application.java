package io.pivotal.payup;

import io.pivotal.payup.persistence.AccountRepository;
import io.pivotal.payup.persistence.TransactionRepository;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.services.TransactionService;
import io.pivotal.payup.services.TransferService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AccountService accountService(AccountRepository accountRepository,TransactionService transactionService) {
        return new AccountService(accountRepository,transactionService);

    }

    @Bean
    public TransferService transferService(AccountRepository accountRepository,TransactionService transactionService)
    {
        return new TransferService(accountRepository, transactionService);
    }

    @Bean
    public TransactionService transactionService(TransactionRepository transactionRepository){
        return new TransactionService(transactionRepository);
    }

}
