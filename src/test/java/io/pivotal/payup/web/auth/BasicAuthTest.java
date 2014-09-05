package io.pivotal.payup.web.auth;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class BasicAuthTest {

    @Test
    public void shouldReturnUsernameAndPasswordFromBasicAuthString() {
        BasicAuth basicAuth = new BasicAuth();
        Credentials credentials = basicAuth.decode("Basic dXNlckBkb21haW4uY29tOmxldHRlcnMxMjNBbmROdW1iZXJzJCHOqQ==");
        assertThat(credentials.getUsername(), equalTo("user@domain.com"));
        assertThat(credentials.getPassword(), equalTo("letters123AndNumbers$!Ω"));
    }
}