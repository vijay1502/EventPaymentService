package com.digibusy.EventEverPaymentService.RPC;

import com.digibusy.EventEverPaymentService.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RazorpayPaymentClient {

    private static final Logger log = LoggerFactory.getLogger(RazorpayPaymentClient.class);

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.enable-mock}")
    private boolean enableMock;

    public String createOrder(long amount) {
        if (enableMock) {
            String mockOrderId = "order_" + UUID.randomUUID();
            log.info(" Mock Razorpay Order Created: {}", mockOrderId);
            return mockOrderId;
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_123456");
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.Orders.create(orderRequest);
            return order.get("id");
        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay order creation failed!", e);
        }
    }
}