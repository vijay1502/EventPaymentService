package com.digibusy.EventEverPaymentService.Controller;

import com.digibusy.EventEverPaymentService.DTO.PaymentRequest;
import com.digibusy.EventEverPaymentService.DTO.PaymentResponse;
import com.digibusy.EventEverPaymentService.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    PaymentService paymentService;


    @PostMapping("/request")
    public ResponseEntity<String> processPayment( @RequestParam String userId,
                                                           @RequestParam Long eventId,
                                                           @RequestParam BigDecimal amount) {
        String orderId = paymentService.processPayment(userId, eventId, amount);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<String> paymentStatus(@PathVariable String orderId){
        return ResponseEntity.ok(paymentService.checkPaymentStatus(orderId));
    }
}
