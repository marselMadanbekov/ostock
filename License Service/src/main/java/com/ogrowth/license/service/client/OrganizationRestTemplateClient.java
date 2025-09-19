package com.ogrowth.license.service.client;

import com.ogrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://gateway-server:8072/organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);
        return restExchange.getBody();
    }
}
