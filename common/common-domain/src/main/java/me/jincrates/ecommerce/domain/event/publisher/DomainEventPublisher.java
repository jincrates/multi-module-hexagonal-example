package me.jincrates.ecommerce.domain.event.publisher;

import me.jincrates.ecommerce.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
