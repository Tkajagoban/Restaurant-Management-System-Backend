package com.restaurent.RMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurent.RMS.utils.DateAudit;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class  Restaurant extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "web_site")
    private String webSite;

    @Column(name = "logo_image")
    private String logoImage;

    @Column(name="email", unique = true)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;
    private String city;

    @OneToMany(mappedBy="restaurant",cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Role> roles;

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MainCategories> mainCategories;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RestaurantPrivilege> restaurantPrivilege;
}
