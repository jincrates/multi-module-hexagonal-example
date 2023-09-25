package me.jincrates.ecommerce.order.dataaccess.outbox.storeapproval.exception;

public class ApprovalOutboxNotFoundException extends RuntimeException{

    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}
