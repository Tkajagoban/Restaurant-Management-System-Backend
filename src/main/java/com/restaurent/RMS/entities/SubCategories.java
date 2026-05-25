package com.restaurent.RMS.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategories extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="subCategories_name")
    private String name;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "mainCategories_id")
    @JsonIgnore
    private MainCategories mainCategories;

}