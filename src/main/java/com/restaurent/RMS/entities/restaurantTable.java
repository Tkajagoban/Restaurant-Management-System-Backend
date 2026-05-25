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
public class restaurantTable extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_number")
    private String tableNumber;
    @Column(name = "guest_count")
    private Integer guestCount;
    private Boolean status;

    @OneToMany(mappedBy = "restaurantTables", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderSummary> orderSummary;
}
