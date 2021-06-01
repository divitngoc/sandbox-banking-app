package com.divitbui.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.exception.TransferFailedException;
import com.divitbui.model.request.AccountRequest;
import com.divitbui.model.request.TransferRequest;
import com.divitbui.model.response.AccountResponse;
import com.divitbui.model.response.AccountsResponse;
import com.divitbui.model.response.TransactionsResponse;
import com.divitbui.service.AccountService;
import com.divitbui.service.TransactionService;
import com.divitbui.service.TransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransferService transferService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> createAccount(final KeycloakAuthenticationToken keyCloakAuthToken,
                                                         @RequestBody final AccountRequest request) {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        final AccountResponse response = accountService.createAccount(token.getSubject(), token.getGivenName(), token.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @PostMapping(value = "{accountId}/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transfer(@Valid @RequestBody final TransferRequest transferRequest,
                                                     @PathVariable final UUID accountId,
                                                     final KeycloakAuthenticationToken keyCloakAuthToken) throws AccountNotFoundException, TransferFailedException {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        transferService.transfer(accountId, token.getSubject(), transferRequest);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping
    public ResponseEntity<AccountsResponse> getAccounts(final KeycloakAuthenticationToken keyCloakAuthToken) {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        final AccountsResponse response = accountService.getAccountsByUserId(token.getSubject());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable final UUID accountId, final KeycloakAuthenticationToken keyCloakAuthToken) throws AccountNotFoundException {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        return ResponseEntity.ok(accountService.getAccountsByUserId(token.getSubject())
                                               .getAccounts()
                                               .stream()
                                               .filter(a -> a.getAccountId().equals(accountId))
                                               .findAny()
                                               .orElseThrow(() -> new AccountNotFoundException("Account ID " + accountId + " not found")));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionsResponse> getTransactionsByAccountId(final KeycloakAuthenticationToken keyCloakAuthToken,
                                                                           @PathVariable final UUID accountId) {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        final TransactionsResponse response = transactionService.getTransactionsByAccountIdAndUserId(accountId, token.getSubject());
        return ResponseEntity.ok(response);
    }
}
