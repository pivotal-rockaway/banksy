package io.pivotal.payup;

import io.pivotal.payup.persistence.UserAccountRepository;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.auth.BasicAuth;
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
    public AccountService accountService(UserAccountRepository userAccountRepository) {
        return new AccountService(userAccountRepository);
    }

    @Bean
    public BasicAuth basicAuth() {
        return new BasicAuth();
    }

}
