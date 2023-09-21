package me.jincrates.ecommerce.infra.saga;

public interface SagaStep<T> {

    void process(T data);

    void rollback(T data);
}
