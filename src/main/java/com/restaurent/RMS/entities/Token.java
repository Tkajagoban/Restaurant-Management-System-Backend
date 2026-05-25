package com.restaurent.RMS.entities;

import com.restaurent.RMS.utils.DateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String type;
    private Boolean revoked = false;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
