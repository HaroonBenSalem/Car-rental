package com.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCarRequest {
    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @Positive(message = "Year must be positive")
    private Integer year;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @Positive(message = "Price per day must be positive")
    private BigDecimal pricePerDay;

    private String description;
}
