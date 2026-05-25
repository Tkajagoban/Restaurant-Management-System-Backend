package com.restaurent.RMS.entities;

import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Email extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "sent_email")
    private String sentEmail;
    @Column(name = "host_name")
    private String hostName;
    private Long port;
    private String protocol;
    private String password;
    @Column(name = "cc_mail_address")
    private String ccMailAddress;
}
