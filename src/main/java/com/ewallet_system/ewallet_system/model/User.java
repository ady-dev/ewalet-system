package com.ewallet_system.ewallet_system.model;

import java.math.BigDecimal;
import com.ewallet_system.ewallet_system.model.base.BaseModelAudit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseModelAudit {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @PrePersist
    public void prePersist() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}