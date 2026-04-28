package com.carrental.repository;

import com.carrental.model.Car;
import com.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByAvailableTrueAndActiveTrue();

    List<Car> findByProvider(User provider);

    List<Car> findByProviderAndActiveTrue(User provider);

    List<Car> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String brand, String model);

    List<Car> findByPricePerDayBetweenAndAvailableTrueAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsByLicensePlate(String licensePlate);
}
