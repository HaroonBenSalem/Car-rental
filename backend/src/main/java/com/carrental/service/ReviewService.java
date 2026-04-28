package com.carrental.service;

import com.carrental.dto.CreateReviewRequest;
import com.carrental.dto.ReviewDTO;
import com.carrental.dto.UserDTO;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.Car;
import com.carrental.model.Review;
import com.carrental.model.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReviewRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Transactional
    public ReviewDTO createReview(Long clientId, CreateReviewRequest request) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + request.getCarId()));

        if (!Boolean.TRUE.equals(car.getActive())) {
            throw new BadRequestException("Cannot review an inactive car");
        }

        if (reviewRepository.existsByClientAndCar(client, car)) {
            throw new BadRequestException("You have already reviewed this car");
        }

        Review review = Review.builder()
                .client(client)
                .car(car)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return toReviewDTO(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        return reviewRepository.findByCar(car)
                .stream()
                .map(this::toReviewDTO)
                .toList();
    }

    private ReviewDTO toReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .client(toUserDTO(review.getClient()))
                .createdAt(review.getCreatedAt())
                .build();
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
