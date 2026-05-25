package com.restaurent.RMS.dtos.response;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDto {
    private Long id;
    private String displayName;
    private String sentEmail;
    private String hostName;
    private Long port;
    private String protocol;
    private String ccMailAddress;
    private String password;
}