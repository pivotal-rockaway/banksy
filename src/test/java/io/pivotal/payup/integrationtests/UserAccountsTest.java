package io.pivotal.payup.integrationtests;

import io.pivotal.payup.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext
public class UserAccountsTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldShowNewUserForm() {
        ResponseEntity<String> getNewAccountResponse = new TestRestTemplate().getForEntity(urlForEndpoint("/accounts/new"), String.class);
        assertThat(getNewAccountResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(getNewAccountResponse.getBody(), containsString("New account"));
    }

    private String urlForEndpoint(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

}
