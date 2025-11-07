package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private BigDecimal amount;
    private String paymentMethod;
    private Byte paymentType; // 1=DEPOSIT, 2=FINAL
    private Byte status;      // 1=PENDING, 2=SUCCESS, 3=FAILED
    private String transactionCode;
    private LocalDateTime createdAt;
}
