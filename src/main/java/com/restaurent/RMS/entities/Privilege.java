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
@lombok.EqualsAndHashCode(callSuper = false)
public class Privilege extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active = true;

    private String name;

    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RestaurantPrivilege> restaurantPrivilege;
}
