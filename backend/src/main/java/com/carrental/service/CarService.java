package com.carrental.service;

import com.carrental.dto.CarDTO;
import com.carrental.dto.CreateCarRequest;
import com.carrental.dto.UserDTO;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.Car;
import com.carrental.model.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CarDTO> getAvailableCars() {
        return carRepository.findByAvailableTrueAndActiveTrue()
                .stream()
                .map(this::toCarDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CarDTO getCarById(Long carId) {
        Car car = findCarEntityById(carId);
        return toCarDTO(car);
    }

    @Transactional(readOnly = true)
    public List<CarDTO> getCarsByProvider(Long providerId) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));

        return carRepository.findByProviderAndActiveTrue(provider)
                .stream()
                .map(this::toCarDTO)
                .toList();
    }

    @Transactional
    public CarDTO createCar(Long providerId, CreateCarRequest request) {
        if (carRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new BadRequestException("License plate already exists");
        }

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));

        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .pricePerDay(request.getPricePerDay())
                .description(request.getDescription())
                .available(true)
                .active(true)
                .provider(provider)
                .build();

        Car savedCar = carRepository.save(car);
        return toCarDTO(savedCar);
    }

    @Transactional
    public CarDTO updateCar(Long carId, Long requesterId, boolean isAdmin, CreateCarRequest request) {
        Car car = findCarEntityById(carId);

        if (!isAdmin && !car.getProvider().getId().equals(requesterId)) {
            throw new BadRequestException("You are not allowed to update this car");
        }

        if (!car.getLicensePlate().equalsIgnoreCase(request.getLicensePlate())
                && carRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new BadRequestException("License plate already exists");
        }

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setLicensePlate(request.getLicensePlate());
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());

        return toCarDTO(carRepository.save(car));
    }

    @Transactional
    public void deleteCar(Long carId, Long requesterId, boolean isAdmin) {
        Car car = findCarEntityById(carId);

        if (!isAdmin && !car.getProvider().getId().equals(requesterId)) {
            throw new BadRequestException("You are not allowed to delete this car");
        }

        car.setActive(false);
        car.setAvailable(false);
        carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public List<CarDTO> searchCars(String query) {
        if (query == null || query.isBlank()) {
            return getAvailableCars();
        }

        return carRepository.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(query, query)
                .stream()
                .filter(car -> Boolean.TRUE.equals(car.getActive()) && Boolean.TRUE.equals(car.getAvailable()))
                .map(this::toCarDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Car findCarEntityById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));
    }

    private CarDTO toCarDTO(Car car) {
        return CarDTO.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .pricePerDay(car.getPricePerDay())
                .description(car.getDescription())
                .available(car.getAvailable())
                .active(car.getActive())
                .averageRating(car.getAverageRating())
                .reviewCount(car.getReviews() != null ? car.getReviews().size() : 0)
                .provider(toUserDTO(car.getProvider()))
                .createdAt(car.getCreatedAt())
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
