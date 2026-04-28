package com.carrental.controller;

import com.carrental.dto.ApiResponse;
import com.carrental.dto.CarDTO;
import com.carrental.dto.CreateCarRequest;
import com.carrental.service.CarService;
import com.carrental.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarDTO>>> getCars(@RequestParam(required = false) String query) {
        List<CarDTO> cars = (query == null || query.isBlank())
                ? carService.getAvailableCars()
                : carService.searchCars(query);

        return ResponseEntity.ok(ApiResponse.success(cars));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarDTO>> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(carService.getCarById(id)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<List<CarDTO>>> getMyCars(Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success(carService.getCarsByProvider(userId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<CarDTO>> createCar(@Valid @RequestBody CreateCarRequest request,
                                                          Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        CarDTO car = carService.createCar(userId, request);
        return ResponseEntity.ok(ApiResponse.success(car, "Car created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<CarDTO>> updateCar(@PathVariable Long id,
                                                          @Valid @RequestBody CreateCarRequest request,
                                                          Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        boolean isAdmin = currentUserService.isAdmin(authentication);
        CarDTO car = carService.updateCar(id, userId, isAdmin, request);
        return ResponseEntity.ok(ApiResponse.success(car, "Car updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable Long id, Authentication authentication) {
        Long userId = currentUserService.getCurrentUserId(authentication);
        boolean isAdmin = currentUserService.isAdmin(authentication);
        carService.deleteCar(id, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Car deleted successfully"));
    }
}
