package me.jincrates.ecommerce.domain.valueobject;

public enum PaymentStatus {
    COMPLETED("결제완료"),
    CANCELLED("결제취소"),
    FAILED("결제실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
