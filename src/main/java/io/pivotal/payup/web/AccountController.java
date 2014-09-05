package io.pivotal.payup.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @RequestMapping(value = "balance", method = RequestMethod.GET)
    public ResponseEntity<String> getBalance() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
