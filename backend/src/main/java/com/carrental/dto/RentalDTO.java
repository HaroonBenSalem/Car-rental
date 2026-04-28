package com.carrental.dto;

import com.carrental.model.RentalStatus;
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
public class RentalDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime returnDate;
    private BigDecimal totalPrice;
    private RentalStatus status;
    private String notes;
    private Long rentalDays;
    private Boolean isOverdue;
    private UserDTO client;
    private CarDTO car;
    private LocalDateTime createdAt;
}
