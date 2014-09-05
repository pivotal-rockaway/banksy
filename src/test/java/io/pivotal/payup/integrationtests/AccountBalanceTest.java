package io.pivotal.payup.integrationtests;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.pivotal.payup.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class AccountBalanceTest {

    @Value("${local.server.port}")
    private int port;

    private Client client;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        client = Client.create();
        gson = new Gson();
    }

    @Test
    public void viewingAccountBalanceWithoutAuthorizationReturns401() {
        WebResource balanceEndpoint = createEndpointClient("/account/balance");
        ClientResponse response = balanceEndpoint.get(ClientResponse.class);
        assertThat(response.getStatus(), equalTo(ClientResponse.Status.UNAUTHORIZED.getStatusCode()));
    }

    private WebResource createEndpointClient(String path) {
        return client.resource(String.format("http://localhost:%d/", port) + path);
    }

}
