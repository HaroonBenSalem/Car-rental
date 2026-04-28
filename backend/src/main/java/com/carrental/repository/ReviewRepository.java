package com.carrental.repository;

import com.carrental.model.Car;
import com.carrental.model.Review;
import com.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCar(Car car);

    List<Review> findByClient(User client);

    Optional<Review> findByClientAndCar(User client, Car car);

    boolean existsByClientAndCar(User client, Car car);
}
