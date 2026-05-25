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
public class MainCategories extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="mainCategories_name")
    private String name;
    private Boolean status;

    @OneToMany(mappedBy = "mainCategories", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SubCategories> subCategories;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "mainCategories", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Food> foods;



}