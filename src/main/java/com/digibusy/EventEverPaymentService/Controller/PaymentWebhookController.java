package com.digibusy.EventEverPaymentService.Controller;

import com.digibusy.EventEverPaymentService.Model.Payment;
import com.digibusy.EventEverPaymentService.Repository.PaymentRepository;
import com.digibusy.EventEverPaymentService.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentWebhookController {
    private static final Logger log = LoggerFactory.getLogger(PaymentWebhookController.class);

    @Autowired
    PaymentRepository paymentRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        try {
            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");

            String orderId = event.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("order_id");

            Optional<Payment> paymentOpt = paymentRepository.findByRazorpayOrderId(orderId);

            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();

                if ("payment.captured".equals(eventType)) {
                    payment.setStatus("SUCCESS");
                    log.info("✅ Payment Captured: Order ID {}", orderId);
                } else if ("payment.failed".equals(eventType)) {
                    payment.setStatus("FAILED");
                    log.error("❌ Payment Failed: Order ID {}", orderId);
                }

                paymentRepository.save(payment);
            } else {
                log.warn("⚠️ No matching payment found for Order ID: {}", orderId);
            }

            return ResponseEntity.ok("Webhook received successfully.");
        } catch (Exception e) {
            log.error("⚠️ Webhook processing failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Webhook failed.");
        }
    }
}
