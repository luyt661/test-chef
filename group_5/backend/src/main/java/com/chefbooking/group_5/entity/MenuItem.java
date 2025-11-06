package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MenuItems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuItemId;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefProfile chef;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String description;
    private BigDecimal price;
    private Double durationHours;
    private Byte status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "menuItem")
    private List<MenuItemImage> images;

    @OneToMany(mappedBy = "menuItem")
    private List<BookingDetail> bookingDetails;
}
