package com.restaurent.RMS.entities;

import com.restaurent.RMS.enums.OrderStatus;
import com.restaurent.RMS.enums.OrderTypes;
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
public class OrderSummary extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private restaurantTable restaurantTables;

    @ManyToOne
    @JoinColumn(name = "steward_id")
    private User steward;

    @OneToMany(mappedBy = "orderSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToMany
    @JoinTable(name = "ordersummary_tax", joinColumns = @JoinColumn(name = "ordersummary_id"), inverseJoinColumns = @JoinColumn(name = "tax_id"))
    private List<Tax> taxes;

    private Long subTotal;
    private Long grandTotal;

    @Enumerated(EnumType.STRING)
    private OrderTypes orderType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
