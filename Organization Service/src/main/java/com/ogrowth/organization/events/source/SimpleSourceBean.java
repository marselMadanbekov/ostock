package com.ogrowth.organization.events.source;

import com.ogrowth.organization.events.model.OrganizationChangeModel;
import com.ogrowth.organization.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleSourceBean {
    private StreamBridge source;

    @Autowired
    public SimpleSourceBean(StreamBridge source) {
        this.source = source;
    }

    public void publishOrganizationChange(String action, String organizationId) {
        log.info("Sending Kafka message {} for OrganizationId: {}", action, organizationId);
        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                organizationId,
                UserContext.getCorrelationId()
        );
        source.send("orgChangeTopic", MessageBuilder.withPayload(change).build());
    }
}
