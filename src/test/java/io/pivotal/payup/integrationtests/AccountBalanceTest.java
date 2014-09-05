package io.pivotal.payup.integrationtests;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.pivotal.payup.Application;
import io.pivotal.payup.json.GetBalanceResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;

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
        WebResource balanceEndpoint = createEndpointClient("/accounts/balance");
        ClientResponse response = balanceEndpoint.get(ClientResponse.class);
        assertThat(response.getStatus(), equalTo(ClientResponse.Status.UNAUTHORIZED.getStatusCode()));
    }

    @Test
    public void viewingAccountBalanceAfterSignUpReturnsZeroBalance() throws IOException {
        WebResource accountsEndpoint = createEndpointClient("/accounts");
        String newUserPayload = readResourceIntoString("/users/user.json");
        ClientResponse newUserResponse = accountsEndpoint.type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, newUserPayload);
        assertThat(newUserResponse.getStatus(), equalTo(ClientResponse.Status.CREATED.getStatusCode()));
        WebResource balanceEndpoint = createEndpointClient("/accounts/balance");
        ClientResponse getBalanceResponse = balanceEndpoint.accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic Ym9iQGVtYWlscHJvdmlkZXIuY29tOnN0cm9uZ3Bhc3N3b3Jk")
                .get(ClientResponse.class);
        assertThat(getBalanceResponse.getStatus(), equalTo(ClientResponse.Status.OK.getStatusCode()));
        String getBalanceResponseBody = getBalanceResponse.getEntity(String.class);
        System.out.println(getBalanceResponseBody);
        GetBalanceResponse getBalanceResponseObject = gson.fromJson(getBalanceResponseBody, GetBalanceResponse.class);
        assertThat(getBalanceResponseObject.getBalance(), equalTo(0L));
    }

    private WebResource createEndpointClient(String path) {
        return client.resource(String.format("http://localhost:%d/", port) + path);
    }

    private String readResourceIntoString(String classpathLocation) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(getClass().getResourceAsStream(classpathLocation), writer);
        return writer.toString();
    }

}
