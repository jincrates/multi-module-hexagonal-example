package me.jincrates.ecommerce.infra.outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}
