package com.restaurent.RMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
public class Role extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="role_name")
    private String roleName;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    @OneToMany(mappedBy ="role",cascade = CascadeType.ALL)
    @JsonIgnore
    private List <User> users;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RolePrivilege> rolePrivileges;

}
