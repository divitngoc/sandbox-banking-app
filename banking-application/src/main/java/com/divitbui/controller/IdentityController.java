package com.divitbui.controller;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divitbui.model.response.UserResponse;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", name = "Authorization")
@RestController
@RequestMapping(value = "/identity", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentityController {

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public ResponseEntity<UserResponse> getIdentity(final KeycloakAuthenticationToken keyCloakAuthToken) {
        final AccessToken token = keyCloakAuthToken.getAccount().getKeycloakSecurityContext().getToken();
        return ResponseEntity.ok(UserResponse.builder()
                                             .id(token.getSubject())
                                             .userName(token.getGivenName())
                                             .email(token.getEmail())
                                             .build());
    }

}
