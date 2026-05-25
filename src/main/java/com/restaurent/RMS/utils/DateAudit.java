package com.restaurent.RMS.utils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class DateAudit {
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private Instant updateAt;

    @PrePersist
    protected void onCreate(){
        createAt = Instant.now();
        updateAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = Instant.now();
    }
}
