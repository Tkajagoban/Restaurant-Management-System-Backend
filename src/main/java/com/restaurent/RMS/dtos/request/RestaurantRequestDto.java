//package com.restaurent.RMS.dtos.request;
//
//import com.restaurent.RMS.utils.ValidationMessages;
//import jakarta.persistence.Lob;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.validator.constraints.URL;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class RestaurantRequestDto {
//
//    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
//    private String name;
//
//    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
//    private String address;
//
//    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
//    @Pattern(regexp="^[0-9]{10}$",message = ValidationMessages.INVALID_FORMAT)
//    private String phoneNumber;
//
//    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
//    @URL(message = ValidationMessages.INVALID_FORMAT)
//    private String webSite;
//
//    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
//    private String city;
//
//
//    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
//    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$" , message = ValidationMessages.INVALID_EMAIL)
//    private String email;
//
//    @Lob
//    private byte[] logoImage;
//}
package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDto {

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = ValidationMessages.INVALID_NAME)
    private String name;

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private String address;

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = ValidationMessages.INVALID_FORMAT)
    private String phoneNumber;

    @Pattern(
            regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = ValidationMessages.INVALID_FORMAT
    )
    private String webSite;

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = ValidationMessages.INVALID_NAME)
    private String city;

    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Pattern(regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = ValidationMessages.INVALID_EMAIL)
    private String email;


    private String logoImage;
}

