package com.divitbui.controller;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divitbui.model.response.TransactionsResponse;
import com.divitbui.service.TransactionService;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", name = "Authorization")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionService transactionService;

    @SecurityRequirement(name = "Authorization")
    @GetMapping()
    public ResponseEntity<TransactionsResponse> getTransactions(final KeycloakAuthenticationToken keyCloakAuthToken) {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        final TransactionsResponse response = transactionService.getTransactionsByUserId(token.getSubject());
        return ResponseEntity.ok(response);
    }

}
