package com.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private BigDecimal pricePerDay;
    private String description;
    private Boolean available;
    private Boolean active;
    private Double averageRating;
    private Integer reviewCount;
    private UserDTO provider;
    private LocalDateTime createdAt;
}
