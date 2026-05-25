package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestaurantResponseDto {
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String webSite;
    private String logoImage;
    private String email;
    private String city;


}
