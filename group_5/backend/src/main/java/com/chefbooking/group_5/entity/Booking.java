package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Entity
@Table(name = "Bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefProfile chef;

    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String eventLocation;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;
    private BigDecimal depositRequired;
    private Byte status;
    private String notes;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "booking")
    private List<BookingDetail> details;

    @OneToOne(mappedBy = "booking")
    private Review review;

    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;

    @OneToMany(mappedBy = "booking")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "booking")
    private List<RefundRequest> refundRequests;
}
