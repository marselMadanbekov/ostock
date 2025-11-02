package com.ogrowth.license.service.client;

import com.ogrowth.license.model.Organization;
import com.ogrowth.license.repository.OrganizationRedisRepository;
import com.ogrowth.license.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final OrganizationRedisRepository redisRepository;

    private final KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        log.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());
        Organization organization = checkRedisCache(organizationId);
        if (organization != null) {
            log.info("I have successfully retrieved an organization {} from redis cache: {}", organizationId, organization);
            return organization;
        }
        log.debug("Unable to locate organization from the redis cache: {}", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://gateway-server:8072/organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);
        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }
        return restExchange.getBody();
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return redisRepository.findById(organizationId).orElse(null);
        } catch (Exception e) {
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}", organizationId, e.getMessage(), e);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
        } catch (Exception e) {
            log.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), e.getMessage(), e);
        }
    }
}
