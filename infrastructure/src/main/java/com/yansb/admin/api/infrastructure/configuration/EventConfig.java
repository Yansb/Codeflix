package com.yansb.admin.api.infrastructure.configuration;

import com.yansb.admin.api.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.yansb.admin.api.infrastructure.configuration.properties.amqp.QueueProperties;
import com.yansb.admin.api.infrastructure.services.EventService;
import com.yansb.admin.api.infrastructure.services.impl.RabbitEventService;
import com.yansb.admin.api.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {
    @Bean
    @VideoCreatedQueue
    @Profile({
            "development",
    })
    EventService localVideoCreatedEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
