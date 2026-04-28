package com.carrental.controller;

import com.carrental.dto.ApiResponse;
import com.carrental.dto.CreateRentalRequest;
import com.carrental.dto.RentalDTO;
import com.carrental.service.CurrentUserService;
import com.carrental.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ApiResponse<RentalDTO>> createRental(@Valid @RequestBody CreateRentalRequest request,
                                                                Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        RentalDTO rental = rentalService.createRental(userId, request);
        return ResponseEntity.ok(ApiResponse.success(rental, "Rental created successfully"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getMyRentals(Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success(rentalService.getClientRentals(userId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RentalDTO>>> getAllRentals() {
        return ResponseEntity.ok(ApiResponse.success(rentalService.getAllRentals()));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<ApiResponse<RentalDTO>> returnRental(@PathVariable Long id,
                                                                Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        boolean isAdmin = currentUserService.isAdmin(authentication);
        RentalDTO rental = rentalService.returnCar(id, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(rental, "Car returned successfully"));
    }
}
