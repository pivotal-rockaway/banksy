package io.pivotal.payup.web;

import com.google.gson.Gson;
import io.pivotal.payup.domain.UnauthorisedAccountAccessException;
import io.pivotal.payup.json.GetBalanceResponse;
import io.pivotal.payup.services.AccountService;
import io.pivotal.payup.web.auth.BasicAuth;
import io.pivotal.payup.web.auth.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final Gson gson;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BasicAuth basicAuth;

    public AccountController() {
        gson = new Gson();
    }

    @RequestMapping(value = "balance", method = RequestMethod.GET)
    public ResponseEntity<String> getBalance(@RequestHeader(value = "Authorization", defaultValue = "") String basicAuthHeader) {
        if (basicAuthHeader.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Credentials credentials = basicAuth.decode(basicAuthHeader);
        try {
            long balance = accountService.getBalance(credentials.getUsername(), credentials.getPassword());
            String balanceResponseJson = gson.toJson(new GetBalanceResponse(balance));
            return new ResponseEntity<>(balanceResponseJson, HttpStatus.OK);
        }
        catch (UnauthorisedAccountAccessException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createUserAccount(@RequestBody String body) {
        @SuppressWarnings("unchecked") Map<String, String> newUserPayload = gson.fromJson(body, Map.class);
        String password = newUserPayload.get("password");
        String username = newUserPayload.get("username");
        accountService.createUserAccount(username, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
