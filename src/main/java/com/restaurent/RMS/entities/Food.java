package com.restaurent.RMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Food extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private Double price;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "mainCategories_id")
    private MainCategories mainCategories;

    @ManyToOne
    @JoinColumn(name = "subcategories_id")
    private SubCategories subCategories;

    // @ManyToMany(mappedBy ="foods",cascade=CascadeType.ALL)
    // private List <OrderItem> orderItem;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItems;
}