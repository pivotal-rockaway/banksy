package io.pivotal.payup.web.auth;

import java.util.Base64;

public class BasicAuth {

    public Credentials decode(String basicAuthHeader) {
        byte[] decodedBytes = Base64.getDecoder().decode(basicAuthHeader.split(" ")[1]);
        String decodedString = new String(decodedBytes);
        String[] userNameAndPassword = decodedString.split(":");
        return new Credentials(userNameAndPassword[0], userNameAndPassword[1]);
    }

}
