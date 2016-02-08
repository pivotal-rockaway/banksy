package io.pivotal.payup;

import io.pivotal.payup.persistence.AccountRepository;
import io.pivotal.payup.services.AccountService;
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
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean
    public TransferService transferService(AccountRepository accountRepository)
    {
        return new TransferService(accountRepository);
    }

}
