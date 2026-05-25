package com.restaurent.RMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tax extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tax_name")
    private String name;
    @Column(name = "tax_percentage")
    private Double percentage;
    private Boolean status;

    @ManyToMany(mappedBy = "taxes")
    @JsonIgnore
    private List<OrderSummary> orderSummaries;
}
