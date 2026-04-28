package com.carrental.repository;

import com.carrental.model.Car;
import com.carrental.model.Rental;
import com.carrental.model.RentalStatus;
import com.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByClient(User client);

    List<Rental> findByCar(Car car);

    List<Rental> findByStatus(RentalStatus status);

    List<Rental> findByClientOrderByCreatedAtDesc(User client);

    List<Rental> findByCarAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Car car,
            List<RentalStatus> statuses,
            LocalDateTime endDate,
            LocalDateTime startDate
    );
}
