package com.digibusy.EventEverPaymentService.Service;

import com.digibusy.EventEverPaymentService.Model.Payment;
import com.digibusy.EventEverPaymentService.RPC.RazorpayPaymentClient;
import com.digibusy.EventEverPaymentService.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    RazorpayPaymentClient razorpayPaymentClient;

    @Autowired
    RestClient restClient;

    public String checkPaymentStatus(String orderId) {
        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();

            if (!"PENDING".equals(payment.getStatus())) {
                return payment.getStatus();
            }

            // ✅ Call Razorpay API to check payment status
            String response = restClient.get()
                    .uri("https://api.razorpay.com/v1/orders/{orderId}/payments", orderId)
                    .retrieve()
                    .body(String.class);
            System.out.println("response:"+response);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getJSONArray("items").length() > 0) {
                JSONObject paymentData = jsonResponse.getJSONArray("items").getJSONObject(0);
                String paymentStatus = paymentData.getString("status");

                if ("captured".equals(paymentStatus)) {
                    payment.setStatus("SUCCESS");
                } else {
                    payment.setStatus("FAILED");
                }

                paymentRepository.save(payment);
                return payment.getStatus();
            }
        }
        return "NOT_FOUND";
    }

    public String processPayment(String userId, Long eventId, BigDecimal amount) {
        String razorpayOrderId = razorpayPaymentClient.createOrder(amount.longValue());

        Payment payment = new Payment();
                payment.setUserId(userId);
                payment.setEventId(eventId);
                payment.setAmount(amount);
                payment.setStatus("PENDING");
                payment.setRazorpayOrderId(razorpayOrderId);

        paymentRepository.save(payment);
        log.info("Payment Initiated: Razorpay Order ID {}", razorpayOrderId);

        return razorpayOrderId;
    }
}

