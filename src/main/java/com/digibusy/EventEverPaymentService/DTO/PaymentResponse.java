package com.digibusy.EventEverPaymentService.DTO;

import lombok.*;


public class PaymentResponse {
    private String status; // SUCCESS, FAILED

    public PaymentResponse() {
    }

    public PaymentResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

