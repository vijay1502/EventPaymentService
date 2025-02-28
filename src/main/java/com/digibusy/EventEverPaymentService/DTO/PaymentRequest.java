package com.digibusy.EventEverPaymentService.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


public class PaymentRequest {
    private String userId;
    private Long eventId;
    private BigDecimal amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String userId, Long eventId, BigDecimal amount) {
        this.userId = userId;
        this.eventId = eventId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "userId='" + userId + '\'' +
                ", eventId=" + eventId +
                ", amount=" + amount +
                '}';
    }
}
