package com.digibusy.EventEverPaymentService.Repository;

import com.digibusy.EventEverPaymentService.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByUserId(String userId);
    @Query("SELECT p from Payment p where p.razorpayOrderId = :razorpayOrderId")
    Optional<Payment> findByRazorpayOrderId(@Param("razorpayOrderId") String razorpayOrderId);

}
