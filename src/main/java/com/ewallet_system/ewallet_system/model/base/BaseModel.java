package com.ewallet_system.ewallet_system.model.base;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
