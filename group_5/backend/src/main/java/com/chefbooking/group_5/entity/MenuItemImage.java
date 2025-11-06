package com.chefbooking.group_5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MenuItem_Images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private String imageUrl;
    private String caption;
}
