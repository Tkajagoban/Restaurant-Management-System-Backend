package com.restaurent.RMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
public class RestaurantPrivilege extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    @OneToMany(mappedBy = "restaurantPrivilege", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RolePrivilege> rolePrivileges;
}